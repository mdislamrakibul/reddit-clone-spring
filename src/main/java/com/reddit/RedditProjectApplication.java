package com.reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.reddit.config.SwaggerConfiguration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@ComponentScan({"com.reddit.mapper","com.reddit.config","com.reddit.controller","com.reddit.dto","com.reddit.exception",
	"com.reddit.filter","com.reddit.model","com.reddit.repository","com.reddit.service","com.reddit.utility",})
@SpringBootApplication
@EnableAsync
//@Import(SwaggerConfiguration.class)
@OpenAPIDefinition
public class RedditProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditProjectApplication.class, args);
	}

}
