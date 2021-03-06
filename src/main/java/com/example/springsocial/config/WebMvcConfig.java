package com.example.springsocial.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	private final long MAX_AGE_SECS = 3600;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
		.allowedHeaders("*")
		.allowCredentials(true)
		.maxAge(MAX_AGE_SECS);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/upload/**")
//		.addResourceLocations("file:///C:/src/instaclone/Mytube/src/main/resources/upload/") // 학원
		.addResourceLocations("file:///C:/workspace-sts4/mytube/src/main/resources/upload/") // 집
		.setCachePeriod(3600)
		.resourceChain(true)
		.addResolver(new PathResourceResolver());
	}
	
//	@Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        MustacheViewResolver resolver = new MustacheViewResolver();
//
//        resolver.setCharset("UTF-8");
//        resolver.setContentType("text/html;charset=UTF-8");
//        resolver.setPrefix("classpath:/templates/");
//        resolver.setSuffix(".html");
//
//        registry.viewResolver(resolver);
//    }
}
