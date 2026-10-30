package com.inventory.fulfillment.repository;
import com.inventory.fulfillment.domain.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<CustomerOrder,Long> {}
