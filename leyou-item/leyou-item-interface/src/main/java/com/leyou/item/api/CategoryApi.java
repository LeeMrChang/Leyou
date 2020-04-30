package com.leyou.item.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName:CategoryApi
 * @Author：Mr.lee
 * @DATE：2020/04/29
 * @TIME： 17:11
 * @Description: TODO
 */
@RequestMapping("category")
public interface CategoryApi {
    /**
     * 根据商品id 查看商品分类的接口
     */
    @GetMapping
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);
}
