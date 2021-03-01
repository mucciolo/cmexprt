package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Order;
import br.net.comexport.api.core.repository.OrderRepository;
import br.net.comexport.api.core.repository.ProductRepository;
import br.net.comexport.api.core.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static br.net.comexport.api.core.http.HttpStatusValue.CREATED_VALUE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("order")
@Api(tags = {"Order API"})
public class OrderController extends CreatelessBaseController<Order, Long, OrderRepository> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public OrderController() {
        super(ExampleMatcher.matching().withIgnoreCase());
    }

    @ApiResponses(value = {
            @ApiResponse(code = CREATED_VALUE, message = "Returns the created object")
    })
    @PutMapping(produces = {"application/json", "text/plain"})
    @ResponseStatus(CREATED)
    public Order create(@RequestBody @Valid final Order.CreationDTO orderCreationDTO) {
        return repository.save(orderCreationDTO.toEntity(userRepository, productRepository));
    }
}