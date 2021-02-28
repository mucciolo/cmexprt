package br.net.comexport.api.core.repository;

import br.net.comexport.api.core.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}