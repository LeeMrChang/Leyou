package com.leyou.item.dto;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * @ClassName:SpuDto
 * @Author：Mr.lee
 * @DATE：2020/04/24
 * @TIME： 10:52
 * @Description: TODO
 */
public class SpuBo extends Spu {
    /**
     * 品牌名称
     */
    private String bname;
    /**
     * 分类名称
     */
    private String cname;

    /**
     * skus 商品sku 集合数据
     * @return
     */
    private List<Sku> skus;

    /**
     * 商品spu  商品的描述表
     * @return
     */
    private SpuDetail spuDetail;

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
