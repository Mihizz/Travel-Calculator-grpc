package com.fon.master.pricing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import io.grpc.Server;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class PricingServiceApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(PricingServiceApplication.class, args);

		Server grpcServer = context.getBean(Server.class);

		try {
			grpcServer.start();
			grpcServer.awaitTermination();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
