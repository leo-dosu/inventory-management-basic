package com.inventory.pos.core.repository;

import com.inventory.pos.core.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllByOrderByNameAsc();
}
