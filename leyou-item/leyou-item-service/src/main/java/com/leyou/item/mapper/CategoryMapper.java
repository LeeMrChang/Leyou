package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName:CategoryMapper
 * @Author：Mr.lee
 * @DATE：2020/04/14
 * @TIME： 17:07
 * @Description: TODO
 */
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
}
