package com.ddl.reggie.service;

import com.ddl.reggie.dto.DishDto;
import com.ddl.reggie.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 10795
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2024-04-24 22:02:58
*/
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入菜品对应口味
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息，同时更新口味信息
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);
}
