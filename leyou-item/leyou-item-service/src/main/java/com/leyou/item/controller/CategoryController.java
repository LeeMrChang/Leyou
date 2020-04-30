package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName:CategoryController
 * @Author：Mr.lee
 * @DATE：2020/04/14
 * @TIME： 17:10
 * @Description: TODO
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点查询商品类目
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> list(@RequestParam(value = "pid",
            defaultValue = "0") Long pid){
        //pid等于空或者小于0 不能成立
        if(pid==null && pid<0){
            //响应400
//            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
            return ResponseEntity.badRequest().build();
        }
        List<Category> list = this.categoryService.queryCategoryByPid(pid);
        //查询出来的集合为null
        if(CollectionUtils.isEmpty(list)){
            //响应500
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //响应的数据
        return ResponseEntity.ok(list);
    }

    /**
     * 根据品牌信息查询商品分类
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid){

        List<Category> list = this.categoryService.queryByBrandId(bid);
        if(CollectionUtils.isEmpty(list)){
            //响应500
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //响应的数据
        return ResponseEntity.ok(list);
    }

    /**
     * 根据商品id 查看商品分类的接口
     */
    @GetMapping
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids){
        List<String> list = this.categoryService.queryNamesByIds(ids);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(list);
    }
}
