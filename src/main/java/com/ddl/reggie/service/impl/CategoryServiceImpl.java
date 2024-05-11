package com.ddl.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddl.reggie.pojo.Category;
import com.ddl.reggie.pojo.Dish;
import com.ddl.reggie.pojo.Setmeal;
import com.ddl.reggie.service.CategoryService;
import com.ddl.reggie.mapper.CategoryMapper;
import com.ddl.reggie.service.DishService;
import com.ddl.reggie.service.SetmealService;
import com.ddl.reggie.utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 10795
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2024-04-24 22:02:58
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除前需要进行判断是否关联菜品
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联菜品，有就抛出一个业务异常
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = (int) dishService.count(dishQueryWrapper);

        //大于0说明有关联菜品
        if (count1>0){
            //抛出一个业务异常
            throw new CustomException("当前分类已关联菜品，不能删除");
        }

        //查询当前分类是否关联套餐，有就抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = (int) setmealService.count(setmealQueryWrapper);
        if (count2>0){
            //抛出一个业务异常
            throw new CustomException("当前分类已关联套餐，不能删除");
        }
        //无关联，正常删除
        super.removeById(id);
    }
}




