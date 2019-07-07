package com.company.chess_online_bakend_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Bartosz Kruba", "", "bartosz.kruba@gmail.com");

        return new ApiInfo(
                "Chess Online REST API",
                "REST API documentation for chess online backend service",
                "1.0",
                "",
                contact,
                "MIT License",
                "https://opensource.org/licenses/MIT",
                new ArrayList<>()
        );
    }
}
