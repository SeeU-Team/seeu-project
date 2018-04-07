//package com.seeu.darkside.gateway.security;
//
//import com.seeu.darkside.gateway.user.UserServiceProxy;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//@Configuration
//@EnableWebSecurity
//@Component
//@Order(1)
//public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	private UserServiceProxy userServiceProxy;
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//				.cors()
//				.and()
//				.csrf()
//					.disable()
//				.authorizeRequests()
//					.antMatchers(HttpMethod.POST, "/login").permitAll()
//					.antMatchers(HttpMethod.GET, "/login").permitAll()
//				.anyRequest().authenticated()
//					.and()
//				.addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), userServiceProxy),
//						AnonymousAuthenticationFilter.class);
//	}
//
//
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration configuration = new CorsConfiguration();
//
//		configuration = configuration.applyPermitDefaultValues();
//
//		source.registerCorsConfiguration("/**", configuration);
//
//		return source;
//	}
//}
