package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.UserServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Component
@EnableWebSecurity
public class WebSecurityConfig {

	@Configuration
	@Order(1)
	public static class LoginConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserServiceProxy userServiceProxy;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.cors()
					.and()
					.csrf()
					.disable()
					.authorizeRequests()
						.antMatchers("/login").permitAll()
						.antMatchers("/actuator/**").permitAll()
					.and()
					.authenticationProvider(new TokenAuthenticationProvider())
					.addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), userServiceProxy),
							AnonymousAuthenticationFilter.class)
					// TODO: check if it works
					.addFilterBefore(new JWTAuthenticationFilter("/**", authenticationManager()),
							AnonymousAuthenticationFilter.class);
		}
	}

//	@Configuration
//	@Order(2)
//	public static class DefaultConfig extends WebSecurityConfigurerAdapter {
//
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http.antMatcher("/**")
//					.authorizeRequests()
//						.antMatchers("/actuator/**").permitAll()
//					//.antMatchers("/**").authenticated()
//					.anyRequest().authenticated()
//					.and()
//					.addFilterBefore(new JWTAuthenticationFilter("/**", authenticationManager()),
//							AnonymousAuthenticationFilter.class);
//		}
//	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();

		// TODO: change cors configuration to minimum needed
		/*
		configuration.addAllowedOrigin("*");
		configuration.addAllowedMethod("GET");
		configuration.addAllowedMethod("POST");
		configuration.addAllowedMethod("PUT");
		configuration.addAllowedMethod("DELETE");
		configuration.addAllowedMethod("OPTIONS");
		*/

		configuration = configuration.applyPermitDefaultValues();

		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
