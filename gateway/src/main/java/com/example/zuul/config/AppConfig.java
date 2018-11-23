package com.example.zuul.config;

import com.example.zuul.jwt.JwtCheckGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gxkai on 2018-11-23 9:24 AM
 **/
@Configuration
public class AppConfig {
    @Bean
    public JwtCheckGatewayFilterFactory jwtCheckGatewayFilterFactory(){
        return new JwtCheckGatewayFilterFactory();
    }
}
