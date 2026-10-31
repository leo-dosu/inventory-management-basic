package com.inventory.reporting.repository;

import com.inventory.reporting.domain.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    @Query("select distinct o from SalesOrder o left join fetch o.items left join fetch o.customer "
            + "where o.orderDate between :from and :to")
    List<SalesOrder> findWithItemsBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
