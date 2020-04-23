package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName:BrandController
 * @Author：Mr.lee
 * @DATE：2020/04/17
 * @TIME： 16:05
 * @Description: TODO
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 根据分页查询品牌信息
     * @param key  关键字查询
     * @param page  当前页
     * @param rows   每页大小
     * @param sortBy  排序字段
     * @param desc   是否为降序
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> page(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", required = false)Boolean desc
    ){
        PageResult<Brand> result = this.brandService.queryBrandsByPage(key,page,rows,sortBy,desc);
        if(CollectionUtils.isEmpty(result.getItems())){
            //响应错误信息
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    /**
     * 新增品牌信息接口
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> save(Brand brand,@RequestParam("cids") List<Long> cids){
        this.brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改品牌信息接口
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> update(Brand brand,@RequestParam("cids") List<Long> cids){
        this.brandService.updateBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除中间表的数据
     */
    @DeleteMapping("cid_bid/{bid}")
    public ResponseEntity<Void> deleteByBrandIdInCategoryBrand(@PathVariable("bid") Long bid){
        this.brandService.deleteByBrandIdInCategoryBrand(bid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 品牌删除接口
     * 可单个删除  与 批量删除
     */
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deteleBrand(@PathVariable("bid") String bid){
        //如果传过来是多个id，使用,隔开
        String separator = ",";
        if(bid.contains(separator)){
            String[] ids = bid.split(separator);
            for (String id : ids) {
                //批量删除操作
                this.brandService.deleteBrand(Long.parseLong(id));
            }
        }
        else {
            //如果传来只有一个id，格式转换，删除一个
            this.brandService.deleteBrand(Long.parseLong(bid));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
