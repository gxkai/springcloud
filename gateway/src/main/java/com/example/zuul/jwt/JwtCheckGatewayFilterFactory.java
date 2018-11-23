package com.example.zuul.jwt;

import com.alibaba.fastjson.JSON;
import com.example.zuul.dto.JsonPackage;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * @author gxkai on 2018-11-23 9:24 AM
 **/
public class JwtCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
            //校验jwtToken的合法性
            if (Jjwt.verifyJwt(jwt)) {
                //合法
                return chain.filter(exchange);
            }
            //不合法
            ServerHttpResponse response = exchange.getResponse();
            //设置headers
            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
            httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            //设置body
            JsonPackage jsonPackage = new JsonPackage();
            jsonPackage.setStatus(110);
            jsonPackage.setMessage("未登录或登录超时");
            DataBuffer bodyDataBuffer = response.bufferFactory().wrap(JSON.toJSONString(jsonPackage).getBytes());
            return response.writeWith(Mono.just(bodyDataBuffer));
        };
    }
}
