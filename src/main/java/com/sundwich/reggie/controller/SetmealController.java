package com.sundwich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sundwich.reggie.common.R;
import com.sundwich.reggie.dto.SetmealDto;
import com.sundwich.reggie.entity.Setmeal;
import com.sundwich.reggie.entity.SetmealDish;
import com.sundwich.reggie.service.SetmealDishService;
import com.sundwich.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;

/**
 * 套餐管理
 *
 * @author JX Sun
 * @date 2023.07.28 11:30
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    SetmealService setmealService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("新增套餐：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐的分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器对象
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealDto> pageDtoInfo=new Page<>();
        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件 进行模糊查询
        lambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        //添加排序条件，根据时间降序排列
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,lambdaQueryWrapper);

        List<SetmealDto> dtoRecords=pageInfo.getRecords().stream().map((item)->{
           SetmealDto dto=new SetmealDto();
           BeanUtils.copyProperties(item,dto);
           dto.setCategoryName(item.getName());
           return dto;
        }).collect(Collectors.toList());
        pageDtoInfo.setRecords(dtoRecords);
        return R.success(pageDtoInfo);
    }

    @DeleteMapping
    //前端发来的参数类型并不是JSon 此时要接收List类型的参数必须加上@RequestParam注解 springboot将自动将请求中的参数解析为一个List<Long>类型的对象
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        setmealService.deleteWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    /**
     * 停售套餐
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> stopSale(@RequestParam List<Long> ids){
        LambdaUpdateWrapper<Setmeal> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(Setmeal::getId,ids);
        lambdaUpdateWrapper.set(Setmeal::getStatus,0);
        setmealService.update(lambdaUpdateWrapper);
        return R.success("停售成功");

    }

    /**
     * 启售套餐
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> startSale(@RequestParam List<Long> ids){
        LambdaUpdateWrapper<Setmeal> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(Setmeal::getId,ids);
        lambdaUpdateWrapper.set(Setmeal::getStatus,1);
        setmealService.update(lambdaUpdateWrapper);
        return R.success("启售成功");

    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value="setmealCache",key="#setmeal.getCategoryId()+'_'+#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
            LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
            queryWrapper.eq(setmeal.getStatus()!=null, Setmeal::getStatus,setmeal.getStatus());
            queryWrapper.orderByDesc(Setmeal::getUpdateTime);
            List<Setmeal> list=setmealService.list(queryWrapper);
            return R.success(list);
    }
}
