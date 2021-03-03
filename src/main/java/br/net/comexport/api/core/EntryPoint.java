package br.net.comexport.api.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

import static br.net.comexport.api.core.controller.OrderController.TAG_ORDER_API;
import static br.net.comexport.api.core.controller.ProductController.TAG_PRODUCT_API;
import static br.net.comexport.api.core.controller.UserController.TAG_USER_API;
import static br.net.comexport.api.core.http.HttpStatusValue.BAD_REQUEST_VALUE;
import static br.net.comexport.api.core.http.HttpStatusValue.INTERNAL_SERVER_ERROR_VALUE;
import static java.util.Arrays.asList;
import static org.springframework.boot.SpringApplication.run;
import static org.springframework.http.HttpMethod.*;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@SpringBootApplication(scanBasePackages = {"br.net.comexport.api.core"})
@Configuration
public class EntryPoint {

    private static final String API_PACKAGE = "br.net.comexport.api.core.controller";

    private static final List<Response> RESPONSE_LIST =
            asList(new ResponseBuilder()
                           .code(String.valueOf(BAD_REQUEST_VALUE))
                           .description("Malformed syntax or invalid request")
                           .build(),
                   new ResponseBuilder()
                           .code(String.valueOf(INTERNAL_SERVER_ERROR_VALUE))
                           .description("Internal server error")
                           .build());

    @Autowired
    private Environment env;

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
                .apiInfo(buildApiInfo())
                .useDefaultResponseMessages(false)
                .globalResponses(GET, RESPONSE_LIST)
                .globalResponses(PUT, RESPONSE_LIST)
                .globalResponses(DELETE, RESPONSE_LIST)
                .tags(new Tag(TAG_USER_API, "Service reponsible for user management"),
                      new Tag(TAG_PRODUCT_API, "Service reponsible for product management"),
                      new Tag(TAG_ORDER_API, "Service reponsible for order placement and update"));
    }

    public ApiInfo buildApiInfo() {

        return new ApiInfoBuilder()
                .title("Comexport ordering REST service")
                .version(env.getProperty("api.version"))
                .contact(new Contact("Diego D. Mucciolo", "https://github.com/mucciolo", "diego.mucciolo@hotmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}