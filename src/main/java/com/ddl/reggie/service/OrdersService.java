package com.ddl.reggie.service;

import com.ddl.reggie.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 10795
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2024-04-24 22:02:58
*/
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);
}
