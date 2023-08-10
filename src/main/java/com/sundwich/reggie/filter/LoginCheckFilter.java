package com.sundwich.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.sundwich.reggie.common.BaseContext;
import com.sundwich.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登陆
 *
 * @author JX Sun
 * @date 2023.07.14 15:47
 */

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 这个类可以在响应到达服务器之前进行处理 或者是响应到达客户端之后进行处理 在调用filterChain.doFilter()之前的处理就是预处理 在那之后的处理就是后处理
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //因为父类没有getRequestURL方法 所以需要强制向下转型
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        1.获取本次请求的url
        String requestURI = request.getRequestURI();
        log.info("拦截到请求:{}",requestURI);
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
//        2. 判断本次请求是否需要处理
        boolean check=check(urls,requestURI);

        //3. 如果不需要处理
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4-1. 判断登陆状态，如果已经登陆，则直接放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登陆，用户id为:{}",request.getSession().getAttribute("employee"));

            //对ThreadLocal进行设置
            Long empId=(Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //4-2. 判断移动端用户登陆状态，如果已经登陆，则直接放行
        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登陆，用户id为:{}",request.getSession().getAttribute("user"));

            //对ThreadLocal进行设置
            Long userId=(Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        //5.如果未登陆则返回未登录结果，通过输出流的方式向客户端页面响应数据
        //和前端是对应的
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查此次请求是否需要放行
     *
     * @param requestURI
     * @return
     */

    public boolean check(String[] urls,String requestURI) {

        for(String url:urls){
           boolean match= PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }

}
