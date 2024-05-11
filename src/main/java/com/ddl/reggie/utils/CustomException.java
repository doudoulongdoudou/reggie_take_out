package com.ddl.reggie.utils;

/**
 * ClassName: CustomException
 * Package: com.ddl.reggie.utils
 * Description:
 * 自定义异常类
 * @Author 豆豆龙
 * @Create 28/4/2024 下午9:39
 */

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
