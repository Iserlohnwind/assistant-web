package com.momassistant.starter;

import com.momassistant.mapper.UserSessionMapper;
import com.momassistant.mapper.model.UserSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ComponentScan(basePackages = "com.momassistant")
@MapperScan("com.momassistant.mapper")
public class AssistantWebApplicationTests {

	@Autowired
	private UserSessionMapper userSessionMapper;
	@Test
	public void contextLoads() {
	}

	@Test
	public void testUserInfo() {
		UserSession userSession = userSessionMapper.findByUserIdAndToken(1, "aaa");
		System.out.println(userSession == null);
	}
}
