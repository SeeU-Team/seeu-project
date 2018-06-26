package com.seeu.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MediaManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaManagementApplication.class, args);
	}
}
