package com.leyou.item.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName:SpecGroup
 * @Author：Mr.lee
 * @DATE：2020/04/23
 * @TIME： 16:55
 * @Description: TODO
 */
@Table(name = "tb_spec_group")
public class SpecGroup implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;

    /**
     * 此注解表示表中没有这个字段，忽烈此字段
     */
    @Transient
    private List<SpecParam> params;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecParam> getParams() {
        return params;
    }

    public void setParams(List<SpecParam> params) {
        this.params = params;
    }
}
