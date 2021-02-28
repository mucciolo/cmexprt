package br.net.comexport.api.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.springframework.boot.SpringApplication.run;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@SpringBootApplication
@Configuration
@EnableSwagger2
public class EntryPoint {

    public static void main(final String[] args) {
        run(EntryPoint.class, args);
    }

    private static final String API_PACKAGE = "br.net.comexport.api.core.controller";

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(basePackage(API_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }
}