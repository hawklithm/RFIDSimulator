package com.hawklithm.bluehawky.center;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainController {
	public static void main(String args[]){
		ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("spring-context-main.xml");
	}
}
