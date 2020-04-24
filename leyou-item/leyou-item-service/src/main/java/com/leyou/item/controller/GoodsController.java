package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.dto.SpuDto;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName:GoodsController
 * @Author：Mr.lee
 * @DATE：2020/04/24
 * @TIME： 10:55
 * @Description: TODO
 */
@RestController
@RequestMapping("spu")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 商品 spu 的按条件 且分页查询
     * @param key   按照标题查询
     * @param saleable  按照是否上下架查询
     * @param page   当前页
     * @param rows   当前页显示页数
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<SpuDto>> page(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows
            ){
        PageResult<SpuDto> result = this.goodsService.querySpuByPage(key,saleable,page,rows);
        if(CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(result);
    }

}
