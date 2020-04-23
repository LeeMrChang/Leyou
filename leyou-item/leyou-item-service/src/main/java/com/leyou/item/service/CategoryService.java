package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

/**
 * @ClassName:CategoryService
 * @Author：Mr.lee
 * @DATE：2020/04/14
 * @TIME： 17:09
 * @Description: TODO
 */
public interface CategoryService {
    List<Category> queryCategoryByPid(Long pid);

    List<Category> queryByBrandId(Long bid);
}
