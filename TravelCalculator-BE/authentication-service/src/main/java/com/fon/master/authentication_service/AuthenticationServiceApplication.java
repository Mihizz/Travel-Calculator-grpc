package com.fon.master.authentication_service;

import io.grpc.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthenticationServiceApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AuthenticationServiceApplication.class, args);

		Server grpcServer = context.getBean(Server.class);

		try {
			grpcServer.start();
			grpcServer.awaitTermination();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
