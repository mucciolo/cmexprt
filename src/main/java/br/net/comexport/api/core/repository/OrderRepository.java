package br.net.comexport.api.core.repository;

import br.net.comexport.api.core.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}