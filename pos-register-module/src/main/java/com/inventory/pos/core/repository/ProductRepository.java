package com.inventory.pos.core.repository;

import com.inventory.pos.core.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByIdAsc();

    Optional<Product> findBySku(String sku);
}
