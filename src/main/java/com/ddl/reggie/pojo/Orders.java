package com.ddl.reggie.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String number;

    private Integer status;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long addressBookId;

    private Date orderTime;

    private Date checkoutTime;

    private Integer payMethod;

    private BigDecimal amount;

    private String remark;

    private String phone;

    private String address;

    private String userName;

    private String consignee;

    private static final long serialVersionUID = 1L;
}