package com.seeu.darkside.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

import static org.springframework.http.HttpMethod.POST;

@SpringBootApplication
@EnableFeignClients
@RestController
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
		return http.httpBasic().disable()
				.csrf().disable()
				.authorizeExchange()
				.pathMatchers(POST, "/login").permitAll()
				.pathMatchers("/actuator/**").permitAll()
				.anyExchange().authenticated()
				.and()
				.securityContextRepository(new ServerSecurityContextRepository() {
					@Override
					public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
						return null;
					}

					@Override
					public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
						final String header = serverWebExchange.getRequest().getHeaders().getFirst("Authorization");

						Authentication authentication = new AbstractAuthenticationToken(Collections.singletonList(new SimpleGrantedAuthority("USER"))) {
							@Override
							public Object getCredentials() {
								return null;
							}

							@Override
							public Object getPrincipal() {
								return null;
							}
						};

						authentication.setAuthenticated(false);

						return Mono.just(new SecurityContextImpl(authentication));
					}
				})
				.build();
	}

	@PostMapping(value = "/login")
	public void login(@RequestBody String accessToken) {
		System.out.println(accessToken);
	}
}
