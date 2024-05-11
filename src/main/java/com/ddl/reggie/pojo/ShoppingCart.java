package com.ddl.reggie.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName shopping_cart
 */
@TableName(value ="shopping_cart")
@Data
public class ShoppingCart implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String name;

    private String image;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dishId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long setmealId;

    private String dishFlavor;

    private Integer number;

    private BigDecimal amount;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}