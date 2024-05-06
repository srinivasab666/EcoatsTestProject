package com.ecoat.management.ecoatapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.util.Arrays;


@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
        		.allowedOriginPatterns("*")
                .allowedMethods("*")
                .maxAge(3600L)
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");


        ResourceProperties resourceProperties = new ResourceProperties();
        final String[] staticLocations = resourceProperties.getStaticLocations();

        // manual add servlet resource location because it had been removed since spring-boot 2.0
        final String[] resourceLocations = Arrays.copyOf(staticLocations, staticLocations.length + 1);
        resourceLocations[staticLocations.length] = "/";

        final String[] indexLocations  = new String[resourceLocations.length];
        for (int i = 0; i < resourceLocations.length; i++) {
            indexLocations[i] = resourceLocations[i] + "index.html";
        }

        String[] paths = {"/login","/auth/**","/pages/**"};
        registry.addResourceHandler(paths)
        .addResourceLocations(indexLocations)
        .resourceChain(true)
        .addResolver(new PathResourceResolver() {
            @Override
            protected Resource getResource(String resourcePath, Resource location) throws IOException {
                return location.exists() && location.isReadable() ? location : null;
            }
        });
    }
}
