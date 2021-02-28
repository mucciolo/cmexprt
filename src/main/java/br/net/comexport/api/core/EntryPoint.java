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

import static java.util.Arrays.asList;
import static org.springframework.boot.SpringApplication.run;
import static org.springframework.http.HttpStatus.*;
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
                           .code(NOT_FOUND.value())
                           .message("Recurso não encontrado.")
                           .build(),
                   new ResponseMessageBuilder()
                           .code(BAD_REQUEST.value())
                           .message("Sintaxe mal formada ou requisição inválida")
                           .build(),
                   new ResponseMessageBuilder()
                           .code(INTERNAL_SERVER_ERROR.value())
                           .message("Erro interno do servidor.")
                           .build());

    private static final ApiInfo API_INFO = new ApiInfoBuilder()
            .title("Comexport order service")
            .version("v1")
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