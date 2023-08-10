package com.sundwich.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sundwich.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author JX Sun
 * @date 2023.08.09 11:03
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
