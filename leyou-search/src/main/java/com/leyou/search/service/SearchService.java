package com.leyou.search.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;

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

    SearchResult search(SearchRequest searchRequest);

    void createIndex(Long id) throws IOException;

    void deleteIndex(Long id);
}
