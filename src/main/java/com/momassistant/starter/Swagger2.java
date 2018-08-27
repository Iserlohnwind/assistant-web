package com.momassistant.starter;

/**
 * Created by zhufeng on 2018/8/19.
 */

//swagger2的配置文件，在项目的启动类的同级文件建立

import com.momassistant.iterceptor.TokenValidateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
public class Swagger2 extends WebMvcConfigurationSupport{
    //swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.momassistant.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("宝妈小闹钟api文档")
                //创建人
                .contact(new Contact("zhufeng", "", ""))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenValidateInterceptor()).addPathPatterns("/**");
        System.out.println("===========   拦截器注册完毕   ===========");
    }

}
