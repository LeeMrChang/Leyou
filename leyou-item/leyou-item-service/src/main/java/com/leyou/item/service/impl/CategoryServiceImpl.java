package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName:CategoryServiceImpl
 * @Author：Mr.lee
 * @DATE：2020/04/14
 * @TIME： 17:09
 * @Description: TODO
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父节点查询商品类目
     * @param pid
     * @return
     */
    @Override
    public List<Category> queryCategoryByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    /**
     * 根据品牌信息查询商品分类
     * @param bid
     * @return
     */
    @Override
    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    /**
     * 根据多个分类id查出 对应过个分类名称
     * @param asList
     * @return
     */
    @Override
    public List<String> queryNamesByIds(List<Long> asList) {
        List<Category> categories = this.categoryMapper.selectByIdList(asList);
        //将集合中的 元素赋值到另一个集合中 需要string类型的元素
        List<String> strings = categories.stream().map(category -> category.getName()).collect(Collectors.toList());
        return strings;
    }


}
