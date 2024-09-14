package com.SpringGateway.Spring.API.Gateway;

import com.SpringGateway.Spring.API.Gateway.filter.ApiKeyFilter;

import com.SpringGateway.Spring.API.Gateway.filter.IpFilter;
import com.SpringGateway.Spring.API.Gateway.filter.JwtFilter;
import com.SpringGateway.Spring.API.Gateway.filter.TimeoutFilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
public class SpringApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringApiGatewayApplication.class, args);
	}


	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder, ApiKeyFilter apiKeyFilter, TimeoutFilter timeoutFilter, IpFilter ipFilter, JwtFilter jwtFilter) {
		System.out.println("RouteLoactor started");
		return builder.routes()
				.route(r -> r
						.path("/delay/{time}")
						.filters(f -> f.filter(apiKeyFilter).filter(timeoutFilter.apply(Duration.ofSeconds(90))))
						.uri("https://httpbin.org"))

				.route(r1 -> r1
						.path("/products")
						.filters(f -> f.filter(apiKeyFilter).filter(ipFilter))
						.uri("https://fakestoreapi.com"))
				.route(r1 -> r1
						.path("/products/{id}")
						.uri("https://fakestoreapi.com"))
				.route(r1 -> r1
						.path("/headers")
						.filters(f -> f.filter(jwtFilter))
						.uri("https://httpbin.org"))
				.build();
	}

}
