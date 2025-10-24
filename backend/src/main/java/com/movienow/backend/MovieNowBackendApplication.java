package com.movienow.backend;

import com.movienow.backend.services.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MovieNowBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieNowBackendApplication.class, args);
	}

}
