package com.leyou.cart.service.impl;

import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName:CartServiceImpl
 * @Author：Mr.lee
 * @DATE：2020/05/20
 * @TIME： 15:50
 * @Description: TODO
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "leyou:cart:uid:";

    static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private GoodsClient goodsClient;

    /**
     * 在redis中添加 购物车的实现
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        //前端传过来的 购物车信息 Map<String,Map<String,String>>
        //通过登录拦截获取用户信息
        UserInfo user = LoginInterceptor.getLoginUser();

        //先查询之前的购物车数据
        BoundHashOperations<String, Object, Object> ops =
                this.redisTemplate.boundHashOps(KEY_PREFIX + user.getId());

        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        //hash 中查询map 中的key是否存在
        Boolean boo = ops.hasKey(skuId.toString());

        //判断商品是否存在
        if(boo){
            //存在 获取redis中购物车数据再修改购物车数量
            String jsonCart = ops.get(skuId.toString()).toString();
            //需要转json序列化
            cart = JsonUtils.parse(jsonCart, Cart.class);
            cart.setNum(cart.getNum()+num);

        }else {
            //不存在 新建一条数据，写入redis
            cart.setUserId(user.getId());
            // 其它商品信息，需要查询商品服务
            Sku sku = this.goodsClient.querySkuById(cart.getSkuId());
            cart.setSkuId(sku.getId());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" :
                    StringUtils.split(sku.getImages(),",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
        }

        //不管是新增还是修改 都需要将购物车信息写入redis
        ops.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));

    }

    /**
     * 查询购物车列表
     * @param
     * @return
     */
    @Override
    public List<Cart> getCart() {
        //获取用户信息
        UserInfo user = LoginInterceptor.getLoginUser();

        String key = KEY_PREFIX + user.getId();

        //判断用户是否有加入购物车的信息
        if(!this.redisTemplate.hasKey(key)){
                return null;
        }

        BoundHashOperations<String, Object, Object> ops =
                this.redisTemplate.boundHashOps(KEY_PREFIX + user.getId());
        //根据redis api 数据
        List<Object> values = ops.values();
        //判断购物车是否有数据
        if(CollectionUtils.isEmpty(values)){
            return null;
        }

        //反序列化。map 转 list集合
        List<Cart> list =
                values.stream().map(v ->
                        JsonUtils.parse(v.toString(), Cart.class))
                        .collect(Collectors.toList());

        return list;
    }

    /**
     * 修改购物车数量
     * @param cart
     */
    @Override
    public void updateCarts(Cart cart) {
        UserInfo user = LoginInterceptor.getLoginUser();

        //先查询
        BoundHashOperations<String, Object, Object> ops =
                this.redisTemplate.boundHashOps(KEY_PREFIX + user.getId());

        //获取购物车信息
        String cartJson = ops.hasKey(cart.getSkuId().toString()).toString();

        Cart cart1 = JsonUtils.parse(cartJson, Cart.class);

        cart1.setNum(cart.getNum());

        //写入redis
        ops.put(KEY_PREFIX+user.getId(),cart1);
    }

    /**
     * 删除购物车信息
     * @param skuId
     */
    @Override
    public void deleteCart(String skuId) {
        UserInfo user = LoginInterceptor.getLoginUser();
        BoundHashOperations<String, Object, Object> ops =
                this.redisTemplate.boundHashOps(KEY_PREFIX + user.getId());
        ops.delete(skuId);
    }
}
