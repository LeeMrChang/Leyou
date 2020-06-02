package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

/**
 * @ClassName:SpecificationService
 * @Author：Mr.lee
 * @DATE：2020/04/23
 * @TIME： 17:01
 * @Description: TODO
 */
public interface SpecificationService {
    List<SpecGroup> queryGroupsByCid(Long cid);

    List<SpecParam> queryParamByGid(Long gid,Long cid,Boolean generic,Boolean searching);

    void saveGroup(SpecGroup specGroup);

    void updateGroup(SpecGroup specGroup);

    void deleteGroup(Long id);

    void saveParam(SpecParam specParam);

    void updateParam(SpecParam specParam);

    void deleteParam(Long id);

    List<SpecGroup> querySpecsByCid(Long cid);
}
