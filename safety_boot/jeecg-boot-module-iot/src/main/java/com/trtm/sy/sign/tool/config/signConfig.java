package com.trtm.sy.sign.tool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wcx
 * @date 2023/8/15
 * @time 16:38
 */
@Configuration
public class signConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //签名图片
        registry.addResourceHandler("/signimg/**").addResourceLocations("file:D:/suxin/qianzhang/sign/");
    }

}
