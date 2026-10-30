package com.inventory.pos.core.repository;

import com.inventory.pos.core.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select coalesce(sum(p.amount), 0) from Payment p")
    BigDecimal sumAll();
}
