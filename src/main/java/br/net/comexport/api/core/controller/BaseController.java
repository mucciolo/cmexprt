package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Updatable;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

import static br.net.comexport.api.core.http.HttpStatusValue.CREATED_VALUE;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * @param <E>  entity type
 * @param <ID> entity id type
 * @param <R>  entity repository type
 */
public abstract class BaseController<E extends Updatable<E>, ID, R extends JpaRepository<E, ID>>
        extends CreatelessBaseController<E, ID, R> {

    public BaseController(final ExampleMatcher listExampleMatcher) {
        super(listExampleMatcher);
    }

    @ApiResponses(value = {
            @ApiResponse(code = CREATED_VALUE, message = "Returns the created object")
    })
    @PutMapping(produces = {"application/json", "text/plain"})
    @ResponseStatus(CREATED)
    public E create(@RequestBody @Valid final E entity) {
        return repository.save(entity);
    }
}
