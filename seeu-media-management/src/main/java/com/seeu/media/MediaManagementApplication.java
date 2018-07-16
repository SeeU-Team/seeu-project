package com.seeu.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class MediaManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaManagementApplication.class, args);
	}
}
