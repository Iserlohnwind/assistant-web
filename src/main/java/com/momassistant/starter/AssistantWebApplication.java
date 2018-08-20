package com.momassistant.starter;

import com.momassistant.iterceptor.TokenValidateInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.momassistant.mapper")
@ComponentScan(basePackages = "com.momassistant")
@EnableAsync
public class AssistantWebApplication  {

	public static void main(String[] args) {
		SpringApplication.run(AssistantWebApplication.class, args);
	}

}
