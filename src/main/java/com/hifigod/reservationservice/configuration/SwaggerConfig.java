package com.hifigod.reservationservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket reservationApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.hifigod.reservationservice"))
                .build()
                .apiInfo(reservationApiInfo());
    }

    private ApiInfo reservationApiInfo() {
        return new ApiInfo(
                "HiFiGod - Reservation Service",
                "API for Reservation Service",
                "1.0.0",
                "",
                new springfox.documentation.service.Contact("HiFiGod", "", ""),
                "API License",
                "",
                Collections.emptyList()
        );
    }
}
