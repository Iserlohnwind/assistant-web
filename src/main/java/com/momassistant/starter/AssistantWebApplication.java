package com.momassistant.starter;

import com.momassistant.iterceptor.TokenValidateInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
@MapperScan("com.momassistant.mapper")
@ComponentScan(basePackages = "com.momassistant")
public class AssistantWebApplication extends WebMvcConfigurationSupport {

	public static void main(String[] args) {
		SpringApplication.run(AssistantWebApplication.class, args);
	}

	@Bean(name = "tokenValidateInterceptor")
	TokenValidateInterceptor requestInterceptor() {
		return new TokenValidateInterceptor();
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TokenValidateInterceptor()).addPathPatterns("/**");
		System.out.println("===========   拦截器注册完毕   ===========");
	}
}
