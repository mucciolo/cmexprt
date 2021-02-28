package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.User;
import br.net.comexport.api.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.function.Consumer;

import static br.net.comexport.api.core.util.ControllerUtils.deleteFromRepositoryById;
import static br.net.comexport.api.core.util.ControllerUtils.findInRepositoryById;
import static java.lang.String.format;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@RestController
@RequestMapping("user")
public final class UserController {

    private static final String MSG_EMAIL_CHANGE_NOT_ALLOWED = "Changing e-mail is not allowed after creation.";

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User findById(@PathVariable final Long id) {
        return findInRepositoryById(userRepository, id);
    }

    @GetMapping
    public Page<User> list(@RequestParam(required = false) final String name,
                           @RequestParam(required = false) final String email,
                           @RequestParam(required = false) @DateTimeFormat(pattern = User.DATE_PATTERN) final Date birthdate,
                           @RequestParam(defaultValue = "0") final int pageNum,
                           @RequestParam(defaultValue = "10") final int pageSize) {

        final Example<User> userExample = Example.of(new User(name, email, birthdate),
                                                     ExampleMatcher.matching()
                                                                   .withIgnoreCase()
                                                                   .withMatcher(User.NAME, contains()));

        return userRepository.findAll(userExample, PageRequest.of(pageNum, pageSize));
    }

    @PutMapping
    public User save(@RequestBody @Valid final User unsavedUser) {

        if (unsavedUser.getId() != null)
            userRepository.findById(unsavedUser.getId()).ifPresent(throwExceptionIfEmailHasChanged(unsavedUser));

        return userRepository.save(unsavedUser);
    }

    private Consumer<User> throwExceptionIfEmailHasChanged(final User user) {
        return currentlySavedUser -> {
            if (!currentlySavedUser.getEmail().equals(user.getEmail()))
                throw new IllegalArgumentException(MSG_EMAIL_CHANGE_NOT_ALLOWED);
        };
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final Long id) {
        return deleteFromRepositoryById(userRepository, id);
    }
}