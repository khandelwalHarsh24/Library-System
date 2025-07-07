package com.librarysystem.book.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthTokenFilter filter) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	        		.requestMatchers(HttpMethod.POST, "/api/v1/books/add").hasRole("ADMIN")
	        	    .requestMatchers(HttpMethod.PUT, "/api/v1/books/{id}").hasRole("ADMIN")
	        	    .requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasRole("ADMIN")
	        	    .requestMatchers(HttpMethod.GET, "/api/v1/books/**").hasAnyRole("USER", "ADMIN")
	        	    .requestMatchers(HttpMethod.PUT,"/api/v1/books/increment/**").hasAnyRole("USER","ADMIN")
	        	    .requestMatchers(HttpMethod.PUT,"/api/v1/books/decrement/**").hasAnyRole("USER","ADMIN")
	        )
	        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
}
