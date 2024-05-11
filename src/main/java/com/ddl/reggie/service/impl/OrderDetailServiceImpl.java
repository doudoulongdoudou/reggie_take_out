package com.ddl.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddl.reggie.pojo.OrderDetail;
import com.ddl.reggie.service.OrderDetailService;
import com.ddl.reggie.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 10795
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2024-04-24 22:02:58
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




