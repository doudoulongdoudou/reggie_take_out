package com.ddl.reggie.dto;

import com.ddl.reggie.pojo.Setmeal;
import com.ddl.reggie.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * ClassName: SetmealDto
 * Package: com.ddl.reggie.dto
 * Description:
 *
 * @Author 豆豆龙
 * @Create 2/5/2024 下午2:55
 */

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
