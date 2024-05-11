package com.ddl.reggie.utils;

/**
 * ClassName: BaseContext
 * Package: com.ddl.reggie.utils
 * Description:
 * 基于TreadLocal封装的工具类，用于保存和获取当前用户的id
 * @Author 豆豆龙
 * @Create 28/4/2024 下午5:45
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
