package com.leyou.item.api;

import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName:SpecifcationApi
 * @Author：Mr.lee
 * @DATE：2020/04/29
 * @TIME： 17:12
 * @Description: TODO
 */
@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 根据分组id 查询参数规格信息
     * @param gid
     * @return
     */
    @GetMapping("params")
    public List<SpecParam> queryParamByGid(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid", required = false)Long cid,
            @RequestParam(value = "generic", required = false)Boolean generic,
            @RequestParam(value = "searching", required = false)Boolean searching);

}
