package com.sundwich.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sundwich.reggie.entity.User;
import com.sundwich.reggie.mapper.UserMapper;
import com.sundwich.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
