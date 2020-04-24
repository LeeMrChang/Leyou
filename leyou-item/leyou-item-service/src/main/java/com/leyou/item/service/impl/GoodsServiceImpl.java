package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.dto.SpuDto;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Spu;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName:GoodsServiceImpl
 * @Author：Mr.lee
 * @DATE：2020/04/24
 * @TIME： 10:55
 * @Description: TODO
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryService categoryService;

    /**
     * 商品 spu 的按条件 且分页查询
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<SpuDto> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //按照关键字查询
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }

        //按照是否上下架状态查询
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }

        //封装分页结果集
        PageHelper.startPage(page,rows);
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>();

        //这是最后需要返回的结果集
        List<SpuDto> list = new ArrayList<>();
        //拼接品牌名称与分类名称
        spus.forEach(spu -> {
            SpuDto dto = new SpuDto();
            //将spu 与spuDto共同属性赋值到  spuDto中
            BeanUtils.copyProperties(spu,dto);
            //查询品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            dto.setBname(brand.getName());

            //查询分类名称。  这里分类涉及多级分类，需要查询多个分类id
            List<String> names= this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1()
                    ,spu.getCid2(),
                    spu.getCid3()));
            //将string 集合添加进对象 已“-” 隔开
            dto.setCname(StringUtils.join(names,"->"));

            list.add(dto);
        });

        return new PageResult<>(pageInfo.getTotal(),list);
    }
}
