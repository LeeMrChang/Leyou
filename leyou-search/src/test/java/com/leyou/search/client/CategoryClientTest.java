package com.leyou.search.client;

import com.leyou.LeyouSearchApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @ClassName:CategoryClientTest
 * @Author：Mr.lee
 * @DATE：2020/04/30
 * @TIME： 10:49
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchApplication.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    /**
     * 根据分类id 查询分类名称
     */
    @Test
    public void queryNamesByIds(){
        List<String> list = this.categoryClient.queryNamesByIds(Arrays.asList(1L, 2L, 3L));
        list.forEach(System.out::println);
    }
}