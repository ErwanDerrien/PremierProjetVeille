package com.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("landing-page");
		registry.addViewController("/").setViewName("landing-page");
		registry.addViewController("/index.html").setViewName("landing-page");
		registry.addViewController("/secret-list").setViewName("secret-list");
		registry.addViewController("/login-form").setViewName("login-form");
	}

}
