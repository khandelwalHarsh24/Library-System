package com.librarysystem.borrow.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	WebClient webClient;
	
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
        	respondUnauthorized(response, "Unauthorized: Missing or invalid Authorization header");
            return;
        }
            try {
                
                String data = webClient.get()
                    .uri("http://localhost:2000/api/v1/auth/validate-token")
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); 
                String[] userData=data.split(",");
                
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userData[0]));
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userData[1], null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(auth);
                chain.doFilter(request, response);

            } catch (WebClientResponseException e) {
                respondUnauthorized(response, e.getResponseBodyAsString());
            } catch (Exception e) {
                respondUnauthorized(response, "Unexpected error occurred while validating token.");
            }
    }
	
	private void respondUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(message);
    }


}
