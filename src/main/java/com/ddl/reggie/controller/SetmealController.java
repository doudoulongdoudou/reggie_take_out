package com.ddl.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddl.reggie.dto.SetmealDishDto;
import com.ddl.reggie.dto.SetmealDto;
import com.ddl.reggie.pojo.Category;
import com.ddl.reggie.pojo.Dish;
import com.ddl.reggie.pojo.Setmeal;
import com.ddl.reggie.pojo.SetmealDish;
import com.ddl.reggie.service.CategoryService;
import com.ddl.reggie.service.DishService;
import com.ddl.reggie.service.SetmealDishService;
import com.ddl.reggie.service.SetmealService;
import com.ddl.reggie.utils.R;
import com.ddl.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: SetmealController
 * Package: com.ddl.reggie.controller
 * Description:
 * 套餐管理
 *
 * @Author 豆豆龙
 * @Create 2/5/2024 下午3:12
 */

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐信息显示分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, queryWrapper);

        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");

        List<SetmealDto> list = setmealPage.getRecords().stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = setmealDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    /**
     * 套餐逻辑删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> removeById(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Setmeal setmeal = setmealService.getById(ids[i]);
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
            if (setmeal.getStatus() == 1) {
                return R.error("套餐售卖中，无法删除");
            }
            setmealDishService.remove(queryWrapper);
            setmealService.removeById(ids[i]);
        }
        return R.success("套餐删除成功");
    }

    /**
     * 修改套餐 信息回显
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithCategory(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐售卖状态 停售/启售
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> stopOrStart(@PathVariable Integer status, Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Setmeal setmeal = setmealService.getById(ids[i]);
            setmeal.setStatus((status == 0 ? 0 : 1));
            setmealService.updateById(setmeal);
        }
        return R.success("信息更新成功");
    }

    /**
     * 移动端用户首页  套餐展示功能
     *
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#categoryId+ '_'+ #status")
    public R<List<Setmeal>> list(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
        queryWrapper.eq(status != null, Setmeal::getStatus, status);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 套餐详情页
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<SetmealDishDto>> getDishById(@PathVariable Long id) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        List<SetmealDishDto> setmealDishDtoList = list.stream().map((item) -> {
            SetmealDishDto setmealDishDto = new SetmealDishDto();
            BeanUtils.copyProperties(item, setmealDishDto);
            Long dishId = item.getDishId();
            Dish dish = dishService.getById(dishId);
            if (dish != null) {
                String image = dish.getImage();
                setmealDishDto.setImage(image);
            }

            return setmealDishDto;
        }).collect(Collectors.toList());

        return R.success(setmealDishDtoList);
    }


}
