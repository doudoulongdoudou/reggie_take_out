package com.ddl.reggie.dto;

import com.ddl.reggie.pojo.Orders;
import com.ddl.reggie.pojo.OrderDetail;
import lombok.Data;
import java.util.List;

/**
 * ClassName: OrdersDto
 * Package: com.ddl.reggie.dto
 * Description:
 *
 * @Author 豆豆龙
 * @Create 3/5/2024 上午10:46
 */

@Data
public class OrdersDto extends Orders {

    private String name;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;


}
