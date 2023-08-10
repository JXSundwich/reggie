package com.sundwich.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sundwich.reggie.entity.OrderDetail;
import com.sundwich.reggie.mapper.OrderDetailMapper;
import com.sundwich.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author JX Sun
 * @date 2023.08.09 11:06
 */

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
