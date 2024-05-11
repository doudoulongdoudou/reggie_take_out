package com.ddl.reggie.dto;

import com.ddl.reggie.pojo.Dish;
import com.ddl.reggie.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DishDto
 * Package: com.ddl.reggie.dto
 * Description:
 *
 * @Author 豆豆龙
 * @Create 29/4/2024 下午8:58
 */

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
