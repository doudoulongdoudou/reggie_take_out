package com.ddl.reggie.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName order_detail
 */
@TableName(value ="order_detail")
@Data
public class OrderDetail implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String name;

    private String image;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dishId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long setmealId;

    private String dishFlavor;

    private Integer number;

    private BigDecimal amount;

    private static final long serialVersionUID = 1L;
}