package com.leyou.item.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName:SpecParam
 * @Author：Mr.lee
 * @DATE：2020/04/23
 * @TIME： 16:56
 * @Description: TODO
 */
@Table(name = "tb_spec_param")
public class SpecParam implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    /**
     * 此注解表示 此字段在musql中是关键字，需要加这个注解且 这样的特殊符号
     */
    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    //'是否是sku通用属性，true或false'
    private Boolean generic;
    //'是否用于搜索过滤，true或false',
    private Boolean searching;
    private String segments;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNumeric() {
        return numeric;
    }

    public void setNumeric(Boolean numeric) {
        this.numeric = numeric;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getGeneric() {
        return generic;
    }

    public void setGeneric(Boolean generic) {
        this.generic = generic;
    }

    public Boolean getSearching() {
        return searching;
    }

    public void setSearching(Boolean searching) {
        this.searching = searching;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }
}
