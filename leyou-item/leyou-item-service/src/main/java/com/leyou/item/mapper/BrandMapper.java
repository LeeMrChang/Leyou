package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName:BrandMapper
 * @Author：Mr.lee
 * @DATE：2020/04/17
 * @TIME： 16:02
 * @Description: TODO
 */
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 新增商品分类和品牌中间表数据
     * @param cid 商品分类id
     * @param bid 品牌id
     * @return
     */
    @Insert("INSERT INTO tb_category_brand(category_id, brand_id) VALUES (#{cid},#{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据品牌id 删除中间表相关数据
     * @param bid
     */
    @Delete("DELETE FROM tb_category_brand WHERE brand_id = #{bid}")
    void deleteByBrandIdInCategoryBrand(Long bid);

    @Select("SELECT * FROM tb_brand tb INNER JOIN tb_category_brand tcb on tb.id = tcb.brand_id WHERE tcb.category_id=#{cid}")
    List<Brand> queryBrandByCid(@Param("cid")Long cid);
}
