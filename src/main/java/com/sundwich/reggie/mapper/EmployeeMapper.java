package com.sundwich.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sundwich.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author JX Sun
 * @date 2023.07.12 17:13
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
