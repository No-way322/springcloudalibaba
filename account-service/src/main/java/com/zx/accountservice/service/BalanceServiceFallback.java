package com.zx.accountservice.service;

import com.zx.accountservice.pojo.Balance;
import org.springframework.stereotype.Component;

/**
 *当资源的平均响应时间超过阈值500毫秒之后，资源进入准降级状态。如果接下来1秒内持续进入5个请求的RT都持续超过这个阈值，
 * 那么在接下的时间窗口（timeWindow）之内，对这个方法的调用都会自动地熔断。
 *
 * 接下来修改Nacos中配置项payment-service-dev.properties的sleep值，修改为1000，让/pay/balance接口响应前先挂起1秒钟来模拟服务器卡顿情况。
 *
 * 分别启动两个服务，用浏览器打开http://localhost:8081/acc/user?id=1并连续刷新6次以上。可以观察到，从第7次开始接口不再等待1秒后返回，而是快速返回降级后的信息
 */

@Component
public class BalanceServiceFallback implements BalanceService {
    @Override
    public Balance getBalance(Integer id) {
        return new Balance(0, 0, 0, "降级");
    }
}