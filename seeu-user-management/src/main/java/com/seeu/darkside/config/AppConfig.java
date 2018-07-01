package com.seeu.darkside.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     @Value("${cloud.aws.credentials.accessKey}")
     private String accessKey;

     @Value("${cloud.aws.credentials.secretKey}")
     private String secretKey;

     @Value("${cloud.aws.region}")
     private String region;

     @Bean
     public BasicAWSCredentials basicAWSCredentials() {
     return new BasicAWSCredentials(accessKey, secretKey);
     }

     @Bean
     public AmazonS3 amazonS3() {

     AnonymousAWSCredentials anonymousAWSCredentials = new AnonymousAWSCredentials();

     return AmazonS3ClientBuilder.standard()
     .withRegion(Regions.fromName(region))
     .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials()))
     .build();
     }**/

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
