package com.godboot.app;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.godboot.app")
@EnableDubbo(scanBasePackages = "com.godboot.app.service")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
