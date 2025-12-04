package com.atguigu.product.service;


import com.order.Order;

public interface OrderService {
    Order createOrder(Long product, Long userId);
}
