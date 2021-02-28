package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.Product;
import br.net.comexport.api.core.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

import static br.net.comexport.api.core.util.ControllerUtils.*;
import static java.lang.String.format;

@RestController
@RequestMapping("product")
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
    public Product create(@RequestBody @Valid final Product newProduct) {
        return productRepository.save(newProduct);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable final Long id, @RequestBody @Valid final Product updatedProduct) {

        if (!productRepository.existsById(id))
            throw new NoSuchElementException(format(FMT_NOT_FOUND, id));

        updatedProduct.setId(id);
        return productRepository.save(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final Long id) {
        return deleteFromRepositoryById(productRepository, id);
    }
}