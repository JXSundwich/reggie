package com.sundwich.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
//这个注解可以使Servlet（控制器），Filter（过滤器），Listener（监听器）
// 可以直接通过@WebServlet，@WebFilter和@WebListener注解自动注册到Spring容器中
@ServletComponentScan
//开启事务管理
@EnableTransactionManagement
public class ReggieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功...");
    }

}
