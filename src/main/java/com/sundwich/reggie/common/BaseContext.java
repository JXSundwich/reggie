package com.sundwich.reggie.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登陆用户id
 * @author JX Sun
 * @date 2023.07.20 16:53
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
