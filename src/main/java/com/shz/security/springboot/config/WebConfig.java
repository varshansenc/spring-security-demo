package com.shz.security.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 自定义登录页
//        registry.addViewController("/").setViewName("redirect:/login-view");
//        registry.addViewController("/login-view").setViewName("login");

        // 默认登录页
        registry.addViewController("/").setViewName("redirect:/login");
    }

}
