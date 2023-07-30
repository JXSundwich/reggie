package com.sundwich.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sundwich.reggie.entity.Employee;
import com.sundwich.reggie.mapper.EmployeeMapper;
import com.sundwich.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author JX Sun
 * @date 2023.07.12 17:16
 */

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
