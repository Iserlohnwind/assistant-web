package com.momassistant.starter;

import com.momassistant.iterceptor.TokenValidateInterceptor;
import com.momassistant.message.KafkaProducer;
import com.momassistant.message.Todo;
import com.momassistant.utils.SpringContextAware;
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
//		KafkaProducer sender = SpringContextAware.getBean(KafkaProducer.class);

//		for (int i = 0; i < 3; i++) {
//			//调用消息发送类中的消息发送方法
//			Todo todo = new Todo("a","1","2","3");
//			sender.send(todo);
//
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
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
