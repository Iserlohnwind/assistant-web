package com.momassistant.starter;

import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.UserSessionMapper;
import com.momassistant.mapper.model.UserSession;
import com.momassistant.wechat.WeixinSmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ComponentScan(basePackages = "com.momassistant")
@MapperScan("com.momassistant.mapper")
public class AssistantWebApplicationTests {

	@Autowired
	private UserInfoMapper userInfoMapper;
	@Test
	public void contextLoads() {
	}

	@Test
	public void testUserInfo() {
		userInfoMapper.updatePregancyInfo(134,"朱峰","15658066782","安徽省,合肥市,全部", new Date());
	}
}
