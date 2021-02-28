package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Order;
import br.net.comexport.api.core.repository.OrderRepository;
import br.net.comexport.api.core.repository.ProductRepository;
import br.net.comexport.api.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

import static br.net.comexport.api.core.util.ControllerUtils.deleteFromRepositoryById;
import static br.net.comexport.api.core.util.ControllerUtils.findInRepositoryById;
import static java.lang.String.format;

@RestController
@RequestMapping("order")
public class OrderController {

    private static final String FMT_NOT_FOUND = "Order ID %s not found.";

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

        final Example<Order> orderExample = Example.of(orderProbe,
                                                       ExampleMatcher.matching().withIgnoreCase());

        return orderRepository.findAll(orderExample, PageRequest.of(pageNum, pageSize));
    }

    @PutMapping
    public Order create(@RequestBody @Valid final Order.CreationDTO orderCreationDTO) {
        return orderRepository.save(orderCreationDTO.toEntity(userRepository, productRepository));
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable final Long id, @RequestBody @Valid final Order updatedOrder) {

        if (!productRepository.existsById(id))
            throw new NoSuchElementException(format(FMT_NOT_FOUND, id));
        else
            updatedOrder.setId(id);

        return orderRepository.save(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final Long id) {
        return deleteFromRepositoryById(orderRepository, id);
    }
}