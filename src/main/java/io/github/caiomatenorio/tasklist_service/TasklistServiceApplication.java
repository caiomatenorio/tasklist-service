package io.github.caiomatenorio.tasklist_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.github.caiomatenorio.tasklist_service.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class TasklistServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TasklistServiceApplication.class, args);
	}
}
