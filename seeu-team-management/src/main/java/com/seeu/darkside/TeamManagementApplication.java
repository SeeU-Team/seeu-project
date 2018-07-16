package com.seeu.darkside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class TeamManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamManagementApplication.class, args);
	}
}
