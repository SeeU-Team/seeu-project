//package com.seeu.darkside.gateway.security;
//
//import com.seeu.darkside.gateway.user.UserServiceProxy;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.GenericFilterBean;
//
//@Component
//@EnableWebSecurity
//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
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
//				.disable()
//				.authorizeRequests()
//				.antMatchers("/login").permitAll()
//				.antMatchers("/actuator/**").permitAll()
//				.anyRequest().authenticated()
//				.and()
//				.formLogin().disable()
//				.httpBasic().and()
//				.addFilterBefore(new GenericFilterBean(), );
////				.authenticationProvider(authenticationProvider())
////				.addFilterBefore(new JwtLoginFilter("/login", authenticationManager(), userServiceProxy),
////						BasicAuthenticationFilter.class)
////				.addFilterBefore(new JwtAuthenticationFilter(),
////						BasicAuthenticationFilter.class);
//	}
//
////	@Configuration
////	@Order(1)
////	public static class LoginConfig extends WebSecurityConfigurerAdapter {
////
////
////	}
//
////	@Configuration
////	@Order(2)
////	public static class DefaultConfig extends WebSecurityConfigurerAdapter {
////
////		@Override
////		protected void configure(HttpSecurity http) throws Exception {
////			http
////					.cors()
////					.and()
////					.csrf()
////					.disable()
////					.authorizeRequests()
////						.antMatchers("/actuator/**").permitAll()
////						.anyRequest().authenticated()
////						.and()
////					.authenticationProvider(new TokenAuthenticationProvider())
////					.addFilterBefore(new JwtAuthenticationFilter(),
////							AnonymousAuthenticationFilter.class);
////		}
////	}
//
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration configuration = new CorsConfiguration();
//
//		// TODO: change cors configuration to minimum needed
//		/*
//		configuration.addAllowedOrigin("*");
//		configuration.addAllowedMethod("GET");
//		configuration.addAllowedMethod("POST");
//		configuration.addAllowedMethod("PUT");
//		configuration.addAllowedMethod("DELETE");
//		configuration.addAllowedMethod("OPTIONS");
//		*/
//
//		configuration = configuration.applyPermitDefaultValues();
//
//		source.registerCorsConfiguration("/**", configuration);
//
//		return source;
//	}
//}
