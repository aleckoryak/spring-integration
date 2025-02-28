package com.ok.springintegration;

import com.ok.springintegration.manage.DashboardManager;
import com.ok.springintegration.util.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication(scanBasePackages = {"com.ok.springintegration"})
@ImportResource("classpath:META-INF/spring/application.xml")
public class DashboardApplication {

	private static Logger logger = LoggerFactory.getLogger(DashboardApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Dashboard Application...");
		ConfigurableApplicationContext context = SpringApplication.run(DashboardApplication.class, args);
		Environment env = context.getBean(Environment.class);
		logger.info("Open this application in your browser at http://localhost:" + env.getProperty("server.port", "") + ". (Modify port number in src/main/resources/application.properties)");

	}

}
