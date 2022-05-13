package com.hz.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Date;

@Component
@Slf4j
public class MyLogGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("*********MySelf Global Filter: " + new Date());
        // 通过Gateway9527端口的地址访问必须带有uname的key和对应的value
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
        if(uname == null){ // 非法用户不放行
            log.info("********username can not be null");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange); // 合法用户直接放行
    }

    // 返回的数值越小 过滤器链中该filter执行的优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}
