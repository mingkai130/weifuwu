package com.atguigu.order.service.impl;


import com.atguigu.order.feign.ProductFeignClient;
import com.atguigu.order.service.OrderService;
import com.order.Order;
import com.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProductFeignClient productFeignClient;


    @Override
    public Order createOrder(Long productId, Long userId) {
//        Product product = getProductFromRemoteWithBalance(productId);

        // 使用feign远程调用
        Product product = productFeignClient.getProductById(productId);


        Order order = new Order();
        order.setId(1L);
        // 总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setUserId(userId);
        order.setNickName("小明");
        order.setAddress("成都");
        // 商品列表
        order.setProducts(List.of(product));
        return order;
    }

    /**
     * 远程调用获取商品信息
     */
    private Product getProductFromRemote(Long productId) {

        // 1、服务发现
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance serviceInstance = instances.get(0);

        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/product/getProduct/" + productId;
        // 发送远程请求
        log.info("发送远程请求：{}", url);
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }

    /**
     * 远程调用获取商品信息
     */
    private Product getProductFromRemoteWithBalance(Long productId) {

        // 1、服务发现
        String url = "http://service-product/product/getProduct/" + productId;
        // 发送远程请求
        log.info("发送远程请求：{}", url);
        // 发送远程请求：http://service-product/product/getProduct/1
        // restTemplate 自动把 service-product 识别为服务名称，自动找对应的服务进行负载均衡
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }

}
