package com.leyou.search.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;

import java.io.IOException;

/**
 * @ClassName:SearchService
 * @Author：Mr.lee
 * @DATE：2020/04/30
 * @TIME： 11:47
 * @Description: TODO
 */

public interface SearchService {

    Goods buildGoods(Spu spuBo) throws IOException;

    PageResult<Goods> search(SearchRequest searchRequest);
}
