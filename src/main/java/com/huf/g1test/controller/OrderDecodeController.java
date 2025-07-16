package com.huf.g1test.controller;

import com.huf.g1test.service.OrderDecodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderDecodeController {
    @Autowired
    private OrderDecodeService orderDecodeService;

    /**
     * 触发批量订单解码
     * @param count 订单数量
     * @return 每个订单的解码结果
     */
    @GetMapping("/decodeBatch")
    public List<String> decodeBatch(@RequestParam(defaultValue = "10") int count) {
        return orderDecodeService.decodeBatch(count);
    }
} 