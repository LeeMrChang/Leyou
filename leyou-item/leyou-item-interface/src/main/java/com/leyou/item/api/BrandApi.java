package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName:BrandApi
 * @Author：Mr.lee
 * @DATE：2020/04/29
 * @TIME： 17:12
 * @Description: TODO
 */
@RequestMapping("brand")
public interface BrandApi {

    /**
     * 根据品牌id查询品牌信息
     */
    @GetMapping("{id}")
    public Brand queryByBrandId(@PathVariable("id") Long id);
}
