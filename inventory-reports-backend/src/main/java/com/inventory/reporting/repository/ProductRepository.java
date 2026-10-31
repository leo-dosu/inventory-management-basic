package com.inventory.reporting.repository;

import com.inventory.reporting.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
