package com.itmuch.cloud.microservicesimpleprovideruser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MicroserviceSimpleProviderUserApplicationTests {

	@Test
	public void contextLoads() {
		int a = 10;
		changeInt(a);
		System.out.println("a = " + a);
		
		String str = "str";
		changeString(str);
		System.out.println("str = " + str);
		
		StringBuilder sb = new StringBuilder("sb1");
		changeStringBuilder(sb);
		System.out.println("sb = " + sb.toString());
	}
	
	public void changeInt (int a) {
		a = 20;
	}
	
	public void changeString (String str) {
		str = "...";
	}
	
	public void changeStringBuilder (StringBuilder sb) {
		sb.append("...");
		sb = new StringBuilder("after");
	}

}
