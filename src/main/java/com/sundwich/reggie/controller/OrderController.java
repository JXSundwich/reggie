package com.sundwich.reggie.controller;

import com.sundwich.reggie.common.R;
import com.sundwich.reggie.entity.OrderDetail;
import com.sundwich.reggie.entity.Orders;
import com.sundwich.reggie.service.OrderDetailService;
import com.sundwich.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JX Sun
 * @date 2023.08.09 11:07
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        return null;
    }
}
