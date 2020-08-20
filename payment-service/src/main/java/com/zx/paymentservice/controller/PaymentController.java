package com.zx.paymentservice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zx.paymentservice.pojo.Balance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RefreshScope
public class PaymentController {

    @Value("${sleep:0}")
    private int sleep;

    final static Map<Integer, Balance> balanceMap = new HashMap() {{
        put(1, new Balance(1, 10, 1000));
        put(2, new Balance(2, 0, 10000));
        put(3, new Balance(3, 100, 0));
    }
    };


    /**
     * nacos的配置文件作用
     * 在限流设置中，grade=1表示基于QPS/并发数做流量控制，count=1表示阈值为1，即QPS超过1触发。controlBehavior=0表示处理策略是直接拒绝。
     *
     * 参数                 含义                                     选项
     * grade：          限流阈值类型                           0 基于线程数 1 基于QPS
     * count：          限流阈值
     * controlBehavior：QPS流量控制中对超过阈值的流量处理手段   0 直接拒绝  1 Warm Up  2 匀速排队
     * strategy：       调用关系限流策略	                      0 根据调用方限流(limitApp)
     *                                                        1 根据调用链路入口限流
     *                                                        2 具有关系的资源流量控制
     *
     * limitApp         调用来源                               default 不区分调用者
     *                                                        {some_origin_name} 针对特定的调用者
     *                                                        other 针对除 {some_origin_name} 以外的其余调用方
     *
     *
     * @param id
     * @return
     */
    @RequestMapping("/pay/balance")
    @SentinelResource(value = "protected-resource", blockHandler = "handleBlock")
    public Balance getBalance(Integer id) {
        System.out.println("request: /pay/balance?id=" + id + ", sleep: " + sleep);
        if(sleep > 0) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(id != null && balanceMap.containsKey(id)) {
            return balanceMap.get(id);
        }
        return new Balance(0, 0, 0);
    }

    /**
     * Sentinel限流，通过nacaos里的payment-service-flow-rules配置限流参数  注解@SentinelResource生效限流效果
     *
     * Gateway通过内置的RequestRateLimiter过滤器实现限流，使用令牌桶算法，借助Redis保存中间数据。用户可通过自定义KeyResolver设置限流维度
     * 比如： 对请求的目标URL进行限流
     *       对来源IP进行限流
     *       特定用户进行限流
     *
     *
     * 在限流设置中，grade=1表示基于QPS/并发数做流量控制，count=1表示阈值为1，即QPS超过1触发。controlBehavior=0表示处理策略是直接拒绝。
     *
     * 重启payment-service，在浏览器中打开http://localhost:8082/pay/balance?id=1并迅速刷新，可以看到一部分的请求返回数据为handleBlock方法的返回值。
     * @param id
     * @param e
     * @return
     */
    public Balance handleBlock(Integer id, BlockException e) {
        return new Balance(0, 0, 0, "限流zzz");
    }
}
