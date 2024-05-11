package com.ddl.reggie.service;

import com.ddl.reggie.dto.SetmealDto;
import com.ddl.reggie.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 10795
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2024-04-24 22:02:58
*/
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐 同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 修改套餐 信息回显
     * @param id
     * @return
     */
    SetmealDto getByIdWithCategory(Long id);
}
