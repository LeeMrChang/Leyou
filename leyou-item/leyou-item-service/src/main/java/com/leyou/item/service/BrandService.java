package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

/**
 * @ClassName:BrandService
 * @Author：Mr.lee
 * @DATE：2020/04/17
 * @TIME： 16:04
 * @Description: TODO
 */
public interface BrandService {
    PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    void saveBrand(Brand brand, List<Long> cids);

    void updateBrand(Brand brand, List<Long> cids);

    void deleteByBrandIdInCategoryBrand(Long bid);

    void deleteBrand(long id);
}
