package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.User;
import br.net.comexport.api.core.repository.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static br.net.comexport.api.core.util.ControllerUtils.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "user")
@Api(tags = {"User API"})
public class UserController {

    private static final ExampleMatcher LIST_EXAMPLE_MATCHER = ExampleMatcher.matching()

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User findById(@PathVariable final Long id) {
        return findInRepositoryById(userRepository, id);
    }

    @GetMapping
    public Page<User> list(final User user,
                           @RequestParam(defaultValue = "0") final int pageNum,
                           @RequestParam(defaultValue = "10") final int pageSize) {
        return userRepository.findAll(Example.of(user, LIST_EXAMPLE_MATCHER), PageRequest.of(pageNum, pageSize));
    }

    @PutMapping
    @ResponseStatus(CREATED)
    public User create(@RequestBody @Valid final User newUser) {
        return userRepository.save(newUser);
    }

    @PutMapping("/{id}")
    @Transactional
    public User update(@PathVariable final Long id, @RequestBody @Valid final User updatedUser) {
        return updateRepositoryById(userRepository, id, updatedUser);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final Long id) {
        return deleteFromRepositoryById(userRepository, id);
    }
}