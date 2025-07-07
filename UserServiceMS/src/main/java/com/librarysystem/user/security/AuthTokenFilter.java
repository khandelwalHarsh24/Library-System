package com.librarysystem.user.security;

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
		String path=request.getServletPath();
		if (path.contains("/register") || path.contains("/login")) {
	        chain.doFilter(request, response);
	        return;
	    }
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Missing or invalid Authorization header");
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
//                authorities.forEach(authority->System.out.println(authority));
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userData[1], null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Current Auth: " + SecurityContextHolder.getContext().getAuthentication());
                chain.doFilter(request, response);

            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
    }


}
