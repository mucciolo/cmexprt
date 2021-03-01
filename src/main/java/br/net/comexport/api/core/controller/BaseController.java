package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Updatable;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

import static br.net.comexport.api.core.http.HttpStatusValue.CREATED;

public abstract class BaseController<T extends Updatable<T>, ID, R extends JpaRepository<T, ID>>
        extends CreatelessBaseController<T, ID, R> {

    public BaseController(final ExampleMatcher listExampleMatcher) {
        super(listExampleMatcher);
    }

    @ApiResponses(value = {
            @ApiResponse(code = CREATED, message = "Returns the created object"),
    })
    @PutMapping(produces = {"application/json", "text/plain"})
    @ResponseStatus(HttpStatus.CREATED)
    public T create(@RequestBody @Valid final T newUser) {
        return repository.save(newUser);
    }
}
