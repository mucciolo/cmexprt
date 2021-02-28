package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Product;
import br.net.comexport.api.core.repository.ProductRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

import static br.net.comexport.api.core.util.ControllerUtils.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("product")
@Api(tags = {"Product API"})
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}")
    public Product findById(@PathVariable final Long id) {
        return findInRepositoryById(productRepository, id);
    }

    @GetMapping
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    @PutMapping
    @ResponseStatus(CREATED)
    public Product create(@RequestBody @Valid final Product newProduct) {
        return productRepository.save(newProduct);
    }

    @PutMapping("/{id}")
    @Transactional
    public Product update(@PathVariable final Long id, @RequestBody @Valid final Product updatedProduct) {
        return updateRepositoryById(productRepository, id, updatedProduct);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final Long id) {
        return deleteFromRepositoryById(productRepository, id);
    }
}