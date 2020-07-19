package com.example.demo.config;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger2Doc
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class Swagger2Config {
}
