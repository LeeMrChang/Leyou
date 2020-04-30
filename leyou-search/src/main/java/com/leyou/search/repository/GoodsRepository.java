package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @ClassName:GoodsRepository
 * @Author：Mr.lee
 * @DATE：2020/04/30
 * @TIME： 11:39
 * @Description: TODO
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
