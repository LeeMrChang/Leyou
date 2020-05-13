package com.leyou.search.pojo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;
import java.util.Map;

/**
 * @ClassName:SearchResult
 * @Author：Mr.lee
 * @DATE：2020/05/03
 * @TIME： 9:59
 * @Description: TODO
 */
public class SearchResult  extends PageResult<Goods> {

    /**
     *  分类作为桶
     */
    private List<Map<String, Object>> categories;
    /**
     * 品牌作为桶
     */
    private List<Brand> brands;

    /**
     * 参数规格数据
     */
    private List<Map<String, Object>> specs;

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    public SearchResult(List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(List<Goods> content, long totalElements, int totalPages, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
    }

    public SearchResult(Long total, List<Goods> items) {
        super(total, items);
    }

    public SearchResult(Long total, Long totalPage, List<Goods> items) {
        super(total, totalPage, items);
    }

    public SearchResult(List<Goods> content, long totalElements, int totalPages, List<Map<String, Object>> categories, List<Brand> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public List<Map<String, Object>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, Object>> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
}
