package com.leyou.item.dto;

import com.leyou.item.pojo.Spu;

/**
 * @ClassName:SpuDto
 * @Author：Mr.lee
 * @DATE：2020/04/24
 * @TIME： 10:52
 * @Description: TODO
 */
public class SpuDto extends Spu {
    /**
     * 品牌名称
     */
    private String bname;
    /**
     * 分类名称
     */
    private String cname;

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
