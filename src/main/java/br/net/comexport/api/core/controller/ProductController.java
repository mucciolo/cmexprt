package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Product;
import br.net.comexport.api.core.repository.ProductRepository;
import io.swagger.annotations.Api;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
@Api(tags = {"Product API"})
public class ProductController extends BaseController<Product, Long, ProductRepository> {

    public ProductController() {
        super(ExampleMatcher.matching());
    }
}