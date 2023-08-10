package com.sundwich.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sundwich.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author JX Sun
 * @date 2023.08.08 17:45
 */

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
