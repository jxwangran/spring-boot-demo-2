package com.oreo.himalaya.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import com.oreo.himalaya.handler.HelloWorldHandler;

@Configuration
public class Router {

	@Autowired
	private HelloWorldHandler helloWorldHandler;
	
	@Bean
	public RouterFunction<?> routerFunction() {
		return RouterFunctions.route(RequestPredicates.GET("/hello"), helloWorldHandler :: helloworld);
	}
	
	
}
