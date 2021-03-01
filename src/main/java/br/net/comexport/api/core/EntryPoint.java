package br.net.comexport.api.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static br.net.comexport.api.core.http.HttpStatusValue.BAD_REQUEST;
import static br.net.comexport.api.core.http.HttpStatusValue.INTERNAL_SERVER_ERROR;
import static java.util.Arrays.asList;
import static org.springframework.boot.SpringApplication.run;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@SpringBootApplication
@Configuration
@EnableSwagger2
public class EntryPoint {

    private static final String API_PACKAGE = "br.net.comexport.api.core.controller";

    private static final List<ResponseMessage> RESPONSE_MESSAGE_LIST =
            asList(new ResponseMessageBuilder()
                           .code(BAD_REQUEST)
                           .message("Malformed syntax or invalid request")
                           .build(),
                   new ResponseMessageBuilder()
                           .code(INTERNAL_SERVER_ERROR)
                           .message("Internal server error")
                           .build());

    // TODO load from application.properties
    private static final String API_VERSION = "v1";

    private static final ApiInfo API_INFO = new ApiInfoBuilder()
            .title("Comexport order service")
            .version(API_VERSION)
            .build();

    public static void main(final String[] args) {
        run(EntryPoint.class, args);
    }

    @Bean
    public Docket buildSwaggerDocket() {

        return new Docket(SWAGGER_2)
                .select()
                .apis(basePackage(API_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(GET, RESPONSE_MESSAGE_LIST)
                .globalResponseMessage(PUT, RESPONSE_MESSAGE_LIST)
                .globalResponseMessage(DELETE, RESPONSE_MESSAGE_LIST)
                .apiInfo(API_INFO);
    }
}