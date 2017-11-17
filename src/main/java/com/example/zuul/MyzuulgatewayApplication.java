package com.example.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients(basePackages="com.example.zuul.auth.client")
public class MyzuulgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyzuulgatewayApplication.class, args);
	}
	
	/*@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}*/
}
