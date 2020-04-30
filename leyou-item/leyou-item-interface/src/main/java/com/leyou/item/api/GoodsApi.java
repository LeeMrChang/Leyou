package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.dto.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName:GoodsApi
 * @Author：Mr.lee
 * @DATE：2020/04/29
 * @TIME： 17:07
 * @Description: TODO
 */
public interface GoodsApi {

    /**
     * 商品 spu 的按条件 且分页查询
     * @param key   按照标题查询
     * @param saleable  按照是否上下架查询
     * @param page   当前页
     * @param rows   当前页显示页数
     * @return
     */
    @GetMapping("spu/page")
    public PageResult<SpuBo> page(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows
    );

    /**
     * 根据sku_id 查询回显sku  商品规格参数信息
     * @param skuId
     * @return
     */
    @GetMapping("spu/detail/{skuId}")
    public SpuDetail querySpuDetailBySkuId(@PathVariable("skuId") Long skuId);

    /**
     * 根据spuid 查询sku 商品详情的 集合数据
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id")Long id);
}
