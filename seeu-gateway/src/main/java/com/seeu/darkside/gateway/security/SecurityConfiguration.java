package com.seeu.darkside.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.cors.reactive.CorsUtils.isCorsRequest;

@Configuration
public class SecurityConfiguration {

	private final ApiServerSecurityContextRepository securityContextRepository;

	@Autowired
	public SecurityConfiguration(ApiServerSecurityContextRepository securityContextRepository) {
		this.securityContextRepository = securityContextRepository;
	}

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
		return http
				.httpBasic().disable()
				.csrf().disable()
				.addFilterAt(corsFilter(), SecurityWebFiltersOrder.FIRST)
				.authorizeExchange()
					.pathMatchers(POST, "/login/**").permitAll()
//					.pathMatchers(POST, "/admin").permitAll()
					.pathMatchers("/actuator/**").permitAll()
					.pathMatchers("/app/**").permitAll() // authorize web socket URLs
				.anyExchange().authenticated()
				.and()
					.securityContextRepository(securityContextRepository)
				.build();
	}

	private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN";
	private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
	private static final String ALLOWED_ORIGIN = "*";
	private static final String MAX_AGE = "3600";

	@Bean
	public WebFilter corsFilter() {
		return (ServerWebExchange ctx, WebFilterChain chain) -> {
			ServerHttpRequest request = ctx.getRequest();

			if (isCorsRequest(request)) {
				ServerHttpResponse response = ctx.getResponse();
				HttpHeaders headers = response.getHeaders();
				headers.set("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
				headers.set("Access-Control-Allow-Methods", ALLOWED_METHODS);
				headers.set("Access-Control-Max-Age", MAX_AGE);
				headers.set("Access-Control-Allow-Headers", ALLOWED_HEADERS);
				headers.set("Access-Control-Expose-Headers", ALLOWED_HEADERS);

				if (request.getMethod() == HttpMethod.OPTIONS) {
					response.setStatusCode(HttpStatus.OK);
					return Mono.empty();
				}
			}
			return chain.filter(ctx);
		};
	}
}
