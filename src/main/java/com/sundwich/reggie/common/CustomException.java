package com.sundwich.reggie.common;

/**
 * 自定义业务类
 * @author JX Sun
 * @date 2023.07.22 16:07
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
