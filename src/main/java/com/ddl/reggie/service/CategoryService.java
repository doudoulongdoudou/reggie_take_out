package com.ddl.reggie.service;

import com.ddl.reggie.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
* @author 10795
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2024-04-24 22:02:58
*/

@Service
public interface CategoryService extends IService<Category> {
    /**
     * 根据id删除分类，删除前需要进行判断是否关联菜品
     * @param id
     */
    void remove(Long id);
}
