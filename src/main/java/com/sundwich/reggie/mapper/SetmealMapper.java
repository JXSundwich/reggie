package com.sundwich.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sundwich.reggie.entity.Dish;
import com.sundwich.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author JX Sun
 * @date 2023.07.22 11:35
 */

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
