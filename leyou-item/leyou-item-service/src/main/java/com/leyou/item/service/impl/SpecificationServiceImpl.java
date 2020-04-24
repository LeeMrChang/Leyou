package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName:SpecificationServiceImpl
 * @Author：Mr.lee
 * @DATE：2020/04/23
 * @TIME： 17:01
 * @Description: TODO
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据分类id查询 分组信息
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        return this.specGroupMapper.select(group);
    }

    /**
     * 根据分组id 查询参数规格
     * @param gid
     * @return
     */
    @Override
    public List<SpecParam> queryParamByGid(Long gid,Long cid,Boolean generic,Boolean searching) {
        SpecParam param = new SpecParam();
        param.setCid(cid);
        param.setGeneric(generic);
        param.setSearching(searching);
        param.setGroupId(gid);
        return this.specParamMapper.select(param);
    }

    /**
     * 新增分组
     * @param specGroup
     */
    @Override
    public void saveGroup(SpecGroup specGroup) {
        this.specGroupMapper.insert(specGroup);
    }

    /**
     * 修改分组
     * @param specGroup
     */
    @Override
    public void updateGroup(SpecGroup specGroup) {
        this.specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }

    /**
     * 分组删除
     * @param id
     */
    @Override
    public void deleteGroup(Long id) {
        this.specGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增参数
     * @param specParam
     */
    @Override
    public void saveParam(SpecParam specParam) {
        this.specParamMapper.insert(specParam);
    }

    /**
     * 修改参数
     * @param specParam
     */
    @Override
    public void updateParam(SpecParam specParam) {
        this.specParamMapper.updateByPrimaryKeySelective(specParam);
    }

    /**
     * 删除参数
     * @param id
     */
    @Override
    public void deleteParam(Long id) {
        this.specParamMapper.deleteByPrimaryKey(id);
    }
}
