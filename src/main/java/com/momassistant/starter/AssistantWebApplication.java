package com.momassistant.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.momassistant")
public class AssistantWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssistantWebApplication.class, args);
	}
}
