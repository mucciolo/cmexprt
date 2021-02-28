package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Order;
import br.net.comexport.api.core.repository.OrderRepository;
import br.net.comexport.api.core.repository.ProductRepository;
import br.net.comexport.api.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static br.net.comexport.api.core.util.ControllerUtils.deleteFromRepositoryById;
import static br.net.comexport.api.core.util.ControllerUtils.findInRepositoryById;

@RestController
@RequestMapping("{api.ver}/order")
public final class OrderController {

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
    public List<Order> list(final Order order) {

        final Example<Order> orderExample = Example.of(order,
                                                       ExampleMatcher.matching().withIgnoreCase());

        return orderRepository.findAll(orderExample);
    }

    @PutMapping
    public Order save(@RequestBody @Valid final Order.SaveDTO orderSaveDTO) {
        return orderRepository.save(orderSaveDTO.toEntity(userRepository, productRepository));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final Long id) {
        return deleteFromRepositoryById(orderRepository, id);
    }
}