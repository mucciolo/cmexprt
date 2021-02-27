package br.net.comexport.controller;

import br.net.comexport.entity.Order;
import br.net.comexport.repository.OrderRepository;
import br.net.comexport.repository.ProductRepository;
import br.net.comexport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static br.net.comexport.util.ControllerUtils.deleteFromRepositoryById;
import static br.net.comexport.util.ControllerUtils.findInRepositoryById;

@RestController
@RequestMapping("/order")
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

        System.out.println(order.getProduct().getId());

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