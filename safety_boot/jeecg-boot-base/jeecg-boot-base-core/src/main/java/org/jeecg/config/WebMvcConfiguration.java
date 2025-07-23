package org.jeecg.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Spring Boot 2.0 解决跨域问题
 *
 * @Author qinfeng
 *
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${jeecg.path.upload}")
    private String upLoadPath;
    @Value("${jeecg.path.webapp}")
    private String webAppPath;
    @Value("${spring.resource.static-locations}")
    private String staticLocations;

    /**
     * 静态资源的配置 - 使得可以从磁盘中读取 Html、图片、视频、音频等
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:" + upLoadPath + "//", "file:" + webAppPath + "//")
                .addResourceLocations(staticLocations.split(","));
    }

    /**
     * 方案一： 默认访问根路径跳转 doc.html页面 （swagger文档页面）
     * 方案二： 访问根路径改成跳转 index.html页面 （简化部署方案： 可以把前端打包直接放到项目的 webapp，上面的配置）
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("doc.html");
    }

    @Bean
    @Conditional(CorsFilterCondition.class)
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        //是否允许请求带有验证信息
        corsConfiguration.setAllowCredentials(true);
        // 允许访问的客户端域名
        corsConfiguration.addAllowedOrigin("http://z.traiot.cn");
        corsConfiguration.addAllowedOrigin("http://47.96.161.157");
        corsConfiguration.addAllowedOrigin("http://47.96.161.157:8026");
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfiguration.addAllowedOrigin("http://47.98.32.195:8090");
        corsConfiguration.addAllowedOrigin("https://yggc.cncico.com:1080");
        corsConfiguration.addAllowedOrigin("http://121.40.127.185:9084");
        corsConfiguration.addAllowedOrigin("http://122.96.239.81:9090");
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:9080");
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:9029");
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:9025");// 金华基地梁场
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:9037");// 地磅
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:9038");// 桥梁 角度
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:9031");// z 环境
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:9086");// zapp
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:9087");// 47app
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:*");
        corsConfiguration.addAllowedOrigin("http://121.89.166.108:9085");// suxin-app
        corsConfiguration.addAllowedOrigin("https://app.traiot.cn");
        corsConfiguration.addAllowedOrigin("https://app.traiot.cn:8447");
        corsConfiguration.addAllowedOrigin("https://a.traiot.cn");
        corsConfiguration.addAllowedOrigin("https://b.traiot.cn");
        corsConfiguration.addAllowedOrigin("https://vdata.amap.com");
        // corsConfiguration.addAllowedOrigin("http://121.89.166.108*");
        // 允许服务端访问的客户端请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许访问的方法名,GET POST等
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    /**
    * 添加Long转json精度丢失的配置
    * @Return: void
    */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }

    /**
     * SpringBootAdmin的Httptrace不见了
     * https://blog.csdn.net/u013810234/article/details/110097201
     */
    @Bean
    public InMemoryHttpTraceRepository getInMemoryHttpTrace(){
        return new InMemoryHttpTraceRepository();
    }

}
