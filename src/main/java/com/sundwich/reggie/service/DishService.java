package com.sundwich.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sundwich.reggie.dto.DishDto;
import com.sundwich.reggie.entity.Dish;

/**
 * @author JX Sun
 * @date 2023.07.22 11:35
 */

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息和对应的口味信息
    public void updateWithFlavor(DishDto dishDto);
}
