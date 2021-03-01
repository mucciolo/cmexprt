package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Order;
import br.net.comexport.api.core.repository.OrderRepository;
import br.net.comexport.api.core.repository.ProductRepository;
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
@RequestMapping("order")
@Api(tags = {"Order API"})
public class OrderController {

    private static final ExampleMatcher LIST_EXAMPLE_MATCHER = ExampleMatcher.matching().withIgnoreCase();

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}")
    public Order findById(@PathVariable final Long id) {
        return findInRepositoryById(orderRepository, id);
    }

    @GetMapping
    public Page<Order> list(final Order orderProbe,
                            @RequestParam(defaultValue = "0") final int pageNum,
                            @RequestParam(defaultValue = "10") final int pageSize) {
        return orderRepository.findAll(Example.of(orderProbe, LIST_EXAMPLE_MATCHER), PageRequest.of(pageNum, pageSize));
    }

    @PutMapping
    @ResponseStatus(CREATED)
    public Order create(@RequestBody @Valid final Order.CreationDTO orderCreationDTO) {
        return orderRepository.save(orderCreationDTO.toEntity(userRepository, productRepository));
    }

    @PutMapping("/{id}")
    @Transactional
    public Order update(@PathVariable final Long id, @RequestBody @Valid final Order updatedOrder) {
        return updateRepositoryById(orderRepository, id, updatedOrder);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final Long id) {
        return deleteFromRepositoryById(orderRepository, id);
    }
}