package com.atguigu.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;


@SpringBootTest

public class DiscoveryTest {


    @Autowired
    DiscoveryClient discoveryClient;

    @Test
    public void DiscoverProductServiceTest(){

        // 获取服务名称
        for (String service : discoveryClient.getServices()) {
            System.out.println("service: " + service);

        }

    }
}
