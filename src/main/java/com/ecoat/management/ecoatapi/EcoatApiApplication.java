package com.ecoat.management.ecoatapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.ecoat.management.ecoatapi.config.SwaggerConfiguration;


@SpringBootApplication
@Import(SwaggerConfiguration.class)
public class EcoatApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoatApiApplication.class, args);
	}

}
