package com.sundwich.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sundwich.reggie.entity.ShoppingCart;
import com.sundwich.reggie.mapper.ShoppingCartMapper;
import com.sundwich.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author JX Sun
 * @date 2023.08.08 17:47
 */

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService{
}
