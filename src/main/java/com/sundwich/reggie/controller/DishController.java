package com.sundwich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.sundwich.reggie.common.R;
import com.sundwich.reggie.dto.DishDto;
import com.sundwich.reggie.entity.Category;
import com.sundwich.reggie.entity.Dish;
import com.sundwich.reggie.entity.DishFlavor;
import com.sundwich.reggie.service.CategoryService;
import com.sundwich.reggie.service.DishFlavorService;
import com.sundwich.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.certpath.SunCertPathBuilder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜品管理
 * @author JX Sun
 * @date 2023.07.26 10:39
 */

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        //新增菜品，同时插入口味数据
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //因为Dish里面没有CategoryName这个属性 所以必须使用DishDto来传回pageInfo给前端

        //分页构造器
        Page<Dish> pageInfo=new Page<Dish>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();

        lambdaQueryWrapper.like(name!=null,Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,lambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records=pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            //分类id
            Long id=item.getCategoryId();
            //根据id查询分类对象
            Category category=categoryService.getById(id);
            if(category!=null){
                String categoryName=category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;

        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);


        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto=dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        //清除所有的菜品缓存数据
//        Set keys=redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        redisTemplate.delete("dish_"+dishDto.getCategoryId()+"_1");


        return R.success("新增菜品成功");

    }

    /**
     * 停售菜品
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<Dish> stopSale(Long ids){
        Dish dish=dishService.getById(ids);
        dish.setStatus(0);
        dishService.updateById(dish);
        return R.success(dish);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(String ids){
        dishService.removeById(ids);
        return R.success("删除菜品成功");
    }

    /**
     * 根据条件查询菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList=null;
        //先从redis中获取缓存数据
        //动态构造key
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();//dish_1267868973_1 大概这个样子
      //  log.info("菜品状态为：{}",dish.getStatus());
        dishDtoList=(List<DishDto>)redisTemplate.opsForValue().get(key);
        //如果存在，直接返回
        if(dishDtoList!=null){
            return R.success(dishDtoList);
        }

        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis

        //构造查询条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //添加条件 查询状态为1 代表起售的菜品
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list=dishService.list(lambdaQueryWrapper);
        dishDtoList=list.stream().map(item->{
            DishDto dto=new DishDto();
            BeanUtils.copyProperties(item,dto);
            Long categoryId = item.getCategoryId();
            Category category=categoryService.getById(categoryId);
            if(category!=null){
                String categoryName=category.getName();
                dto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId=item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1=new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList=dishFlavorService.list(lambdaQueryWrapper1);
            dto.setFlavors(dishFlavorList);
            return dto;
        }).collect(Collectors.toList());

        //放进Redis缓存
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);


        return R.success(dishDtoList);
    }
}
