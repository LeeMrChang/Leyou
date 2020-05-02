package com.leyou.search.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName:SearchController
 * @Author：Mr.lee
 * @DATE：2020/05/02
 * @TIME： 9:47
 * @Description: TODO
 */
@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 根据关键字查询接口，可能需要分页、条件、聚合
     * 使用SearchRequest对象将要查询的条件封装成一个查询对象
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> page(
            @RequestBody SearchRequest searchRequest){

        PageResult<Goods> pageResult = this.searchService.search(searchRequest);
        if(pageResult == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return  ResponseEntity.ok(pageResult);
    }
}
