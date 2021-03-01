package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.User;
import br.net.comexport.api.core.repository.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@RestController
@RequestMapping(value = "user")
@Api(tags = {"User API"})
public class UserController extends BaseController<User, Long, UserRepository> {

    public UserController() {
        super(ExampleMatcher.matching()
                            .withIgnoreCase()
                            .withMatcher(User.NAME, contains()));
    }
}