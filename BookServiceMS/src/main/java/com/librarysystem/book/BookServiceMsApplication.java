package com.librarysystem.book;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class BookServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookServiceMsApplication.class, args);
	}
	
	@Bean
	public WebClient webClient() {
	    return WebClient.create();
	}
	
	@Bean
	public ModelMapper modelMapper() {
        return new ModelMapper();
    }

	
}
