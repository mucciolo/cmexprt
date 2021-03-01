package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.User;
import br.net.comexport.api.core.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static br.net.comexport.api.core.http.HttpStatusValue.*;
import static br.net.comexport.api.core.util.ControllerUtils.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@RestController
@RequestMapping(value = "user")
@Api(tags = {"User API"})
public class UserController {

    private static final ExampleMatcher LIST_EXAMPLE_MATCHER = ExampleMatcher.matching()
                                                                             .withIgnoreCase()
                                                                             .withMatcher(User.NAME, contains());

    private static final String MSG_NOT_FOUND = "Returns a text message informing an object with ID does not exists";

    @Autowired
    private UserRepository userRepository;

    @ApiResponses(value = {
            @ApiResponse(code = OK, message = "Returns the object with the given ID"),
            @ApiResponse(code = NOT_FOUND, message = MSG_NOT_FOUND)
    })
    @GetMapping(value = "/{id}", produces = {"application/json", "text/plain"})
    public User findById(@PathVariable final Long id) {
        return findInRepositoryById(userRepository, id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = OK, message = "Returns a page of objects satisfying the given filtering criteria")
    })
    @GetMapping(produces = {"application/json"})
    public Page<User> list(final User user,
                           @RequestParam(defaultValue = "0") final int pageNum,
                           @RequestParam(defaultValue = "10") final int pageSize) {
        return userRepository.findAll(Example.of(user, LIST_EXAMPLE_MATCHER), PageRequest.of(pageNum, pageSize));
    }

    @ApiResponses(value = {
            @ApiResponse(code = CREATED, message = "Returns the created object"),
    })
    @PutMapping(produces = {"application/json", "text/plain"})
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid final User newUser) {
        return userRepository.save(newUser);
    }

    @ApiResponses(value = {
            @ApiResponse(code = OK, message = "Returns the updated object"),
            @ApiResponse(code = NOT_FOUND, message = MSG_NOT_FOUND)
    })
    @PutMapping(value = "/{id}", produces = {"application/json", "text/plain"})
    @Transactional
    public User update(@PathVariable final Long id, @RequestBody @Valid final User updatedUser) {
        return updateRepositoryById(userRepository, id, updatedUser);
    }

    @ApiResponses(value = {
            @ApiResponse(code = OK, message = "Returns a text message indicating the deletion was successful"),
            @ApiResponse(code = NOT_FOUND, message = MSG_NOT_FOUND)
    })
    @DeleteMapping(value = "/{id}", produces = {"text/plain"})
    public String delete(@PathVariable final Long id) {
        return deleteFromRepositoryById(userRepository, id);
    }
}