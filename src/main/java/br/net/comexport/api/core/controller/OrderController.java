package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.controller.BaseController.CreateMethod;
import br.net.comexport.api.core.entity.Order;
import br.net.comexport.api.core.repository.OrderRepository;
import br.net.comexport.api.core.repository.ProductRepository;
import br.net.comexport.api.core.repository.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @CreateMethod
    @PutMapping(produces = {"application/json", "text/plain"})
    public Order create(@RequestBody @Valid final Order.CreationDTO orderCreationDTO) {
        return repository.save(orderCreationDTO.toEntity(userRepository, productRepository));
    }
}