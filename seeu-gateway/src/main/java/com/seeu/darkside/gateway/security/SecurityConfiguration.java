package com.seeu.darkside.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfiguration {

	@Autowired
	private ApiServerSecurityContextRepository securityContextRepository;

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
		return http
				.httpBasic().disable()
				.csrf().disable()
				.authorizeExchange()
					.pathMatchers(POST, "/login").permitAll()
					.pathMatchers("/actuator/**").permitAll()
				.anyExchange().authenticated()
				.and()
					.securityContextRepository(securityContextRepository)
				.build();
	}
}
