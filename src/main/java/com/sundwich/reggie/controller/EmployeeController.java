package com.sundwich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sundwich.reggie.common.R;
import com.sundwich.reggie.entity.Dish;
import com.sundwich.reggie.entity.Employee;
import com.sundwich.reggie.service.DishService;
import com.sundwich.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author JX Sun
 * @date 2023.07.12 17:18
 */

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //request用来存储一个session方便后面get使用

    /**
     * 员工登陆
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
//        1.将页面提交的密码password进行md5加密处理
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
//         2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);//此处getOne()因为数据库设定了username为unique
//        3.若果没有没有查询到结果，返回登陆失败
        if(emp==null){
            return R.error("登陆失败");
        }
//        4.密码比对，如果不一致则返回登陆失败结果
        if(!password.equals(emp.getPassword())){
            return R.error("密码错误");
        }
//        5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus()==0){
            return R.error("账号已禁用");
        }
//          6.登陆成功，将员工id存入Session并返回登陆成功结果
            request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息{}",employee.toString());
        //创建初始密码 并进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
       // employee.setCreateTime(LocalDateTime.now());
       // employee.setUpdateTime(LocalDateTime.now());
        //获得当前登陆用户的id
        Long empId=(Long)request.getSession().getAttribute("employee");

       // employee.setCreateUser(empId);
      //  employee.setUpdateUser(empId);

        //save方法不是自己写的 是EmployeeService继承的类里面自带的
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息的分页查询
     * @param request
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

     @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
         log.info("page={},pageSize={},name={}",page,pageSize,name);

         //构造分页构造器
         Page<Employee> pageInfo=new Page(page,pageSize);

         //构造条件构造器
         LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
         //添加过滤条件
         queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
         //添加排序条件
         queryWrapper.orderByDesc(Employee::getUpdateTime);

         //执行查询
         employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);

    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        Long empId=(Long)request.getSession().getAttribute("employee");
         log.info(employee.toString());
        // employee.setUpdateUser((Long)empId);
       //  employee.setUpdateTime(LocalDateTime.now());
         employeeService.updateById(employee);

         return R.success("员工信息修改成功");
    }


    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee=employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息...");



    }


}
