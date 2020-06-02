package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

/**
 * @ClassName:CartService
 * @Author：Mr.lee
 * @DATE：2020/05/20
 * @TIME： 15:49
 * @Description: TODO
 */
public interface CartService {
    void addCart(Cart cart);

    List<Cart> getCart();

    void updateCarts(Cart cart);

    void deleteCart(String skuId);
}
