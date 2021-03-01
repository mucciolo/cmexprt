package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Updatable;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static br.net.comexport.api.core.http.HttpStatusValue.NOT_FOUND;
import static br.net.comexport.api.core.http.HttpStatusValue.OK;
import static br.net.comexport.api.core.util.ControllerUtils.*;

public abstract class CreatelessBaseController<T extends Updatable<T>, ID, R extends JpaRepository<T, ID>> {

    private static final String MSG_NOT_FOUND = "Returns a text message informing an object with ID does not exists";

    private final ExampleMatcher listExampleMatcher;

    @Autowired
    protected R repository;

    protected CreatelessBaseController(final ExampleMatcher listExampleMatcher) {
        this.listExampleMatcher = listExampleMatcher;
    }

    @ApiResponses(value = {
            @ApiResponse(code = OK, message = "Returns the object with the given ID"),
            @ApiResponse(code = NOT_FOUND, message = MSG_NOT_FOUND)
    })
    @GetMapping(value = "/{id}", produces = {"application/json", "text/plain"})
    public T findById(@PathVariable final ID id) {
        return findInRepositoryById(repository, id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = OK, message = "Returns a page of objects satisfying the given filtering criteria")
    })
    @GetMapping(produces = {"application/json"})
    public Page<T> list(final T user,
                           @RequestParam(defaultValue = "0") final int pageNum,
                           @RequestParam(defaultValue = "10") final int pageSize) {
        return repository.findAll(Example.of(user, listExampleMatcher), PageRequest.of(pageNum, pageSize));
    }

    @ApiResponses(value = {
            @ApiResponse(code = OK, message = "Returns the updated object"),
            @ApiResponse(code = NOT_FOUND, message = MSG_NOT_FOUND)
    })
    @PutMapping(value = "/{id}", produces = {"application/json", "text/plain"})
    @Transactional
    public T update(@PathVariable final ID id, @RequestBody @Valid final T updatedUser) {
        return updateRepositoryById(repository, id, updatedUser);
    }

    @ApiResponses(value = {
            @ApiResponse(code = OK, message = "Returns a text message indicating the deletion was successful"),
            @ApiResponse(code = NOT_FOUND, message = MSG_NOT_FOUND)
    })
    @DeleteMapping(value = "/{id}", produces = {"text/plain"})
    public String delete(@PathVariable final ID id) {
        return deleteFromRepositoryById(repository, id);
    }
}