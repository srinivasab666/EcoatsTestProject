package com.ecoat.management.ecoatapi.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	@Bean
	public Docket ecoatApi() {
		return new Docket(DocumentationType.SWAGGER_2).securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey())).
				select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex("/api.*")).build().apiInfo(getApiInfo());
	}
	
	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder().title("ECOAT API").version("1.0").description("API for ECOAT Application")
				.contact(new Contact("Srinivas", "http://ecoat.com", "xyz@email.com"))
				.license("Apache License Version 2.0").build();
	}
	
	private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }
	
	private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
            .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
            new SecurityReference("JWT", authorizationScopes));
    }
}
