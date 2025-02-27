package com.ok.integration.helloworld;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PollerApp {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("META-INF/spring/integration/delay.xml");
    }
}
