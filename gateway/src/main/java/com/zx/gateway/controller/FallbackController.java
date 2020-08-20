package com.zx.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * 网关是所有请求的入口，如果部分后端服务延时严重，则可能导致大量请求堆积在网关上，
 * 拖垮网关进而瘫痪整个系统。这就需要对响应慢的服务做超时快速失败处理，即熔断
 * hystrix 的熔断
 */
@RestController
public class FallbackController {
    @RequestMapping("/defaultFallback")
    public Map defaultFallback() {
        Map map = new HashMap<>();
        map.put("code", 1);
        map.put("message", "服务异常zzz");
        return map;
    }
}
