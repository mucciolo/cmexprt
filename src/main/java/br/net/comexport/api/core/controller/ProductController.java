package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Product;
import br.net.comexport.api.core.repository.ProductRepository;
import br.net.comexport.api.core.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequestMapping("product")
public final class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}")
    public Product findById(@PathVariable final Long id) {
        return ControllerUtils.findInRepositoryById(productRepository, id);
    }

    @GetMapping
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    @PutMapping
    public Product save(@RequestBody @Valid final Product product) {
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final Long id) {
        return ControllerUtils.deleteFromRepositoryById(productRepository, id);
    }
}