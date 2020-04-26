package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.dto.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.util.List;


/**
 * @ClassName:GoodsController
 * @Author：Mr.lee
 * @DATE：2020/04/24
 * @TIME： 10:55
 * @Description: TODO
 */
@Controller
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
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> page(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows
            ){
        PageResult<SpuBo> result = this.goodsService.querySpuByPage(key,saleable,page,rows);
        if(CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增商品信息
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新商品信息
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spubo){
        this.goodsService.updateGoods(spubo);
        //更新成功。响应204
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据sku_id 查询回显sku  商品规格参数信息
     * @param skuId
     * @return
     */
    @GetMapping("spu/detail/{skuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySkuId(@PathVariable("skuId") Long skuId){
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySkuId(skuId);
        if(spuDetail == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据spuid 查询sku 商品详情的 集合数据
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("id")Long id){
        List<Sku> skus = this.goodsService.querySkusBySpuId(id);
        if(CollectionUtils.isEmpty(skus)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(skus);
    }


}
