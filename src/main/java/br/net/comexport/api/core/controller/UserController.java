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
@Api(tags = {UserController.TAG_USER_API})
public class UserController extends BaseController<User, Long, UserRepository> {

    public static final String TAG_USER_API = "User API";

    public UserController() {
        super(ExampleMatcher.matching()
                            .withIgnoreCase()
                            .withMatcher(User.NAME, contains()));
    }
}