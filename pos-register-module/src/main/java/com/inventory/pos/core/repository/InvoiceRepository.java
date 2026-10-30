package com.inventory.pos.core.repository;

import com.inventory.pos.core.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("select i from Invoice i left join fetch i.customer order by i.issuedOn desc, i.id desc")
    List<Invoice> findAllNewestFirst();

    /** Invoices with money still owed. Only customers can owe, because walk-in sales are paid in full. */
    @Query("select i from Invoice i join fetch i.customer where i.total > i.paid order by i.dueOn asc, i.id asc")
    List<Invoice> findOpenWithCustomer();

    @Query("select i from Invoice i where i.customer.id = :customerId order by i.dueOn asc, i.id asc")
    List<Invoice> findByCustomer(@Param("customerId") Long customerId);

    long countByIssuedOnBetween(LocalDate from, LocalDate to);

    @Query("select coalesce(sum(i.total - i.paid), 0) from Invoice i where i.total > i.paid")
    BigDecimal sumOutstanding();

    @Query("select coalesce(sum(i.total - i.paid), 0) from Invoice i where i.total > i.paid and i.dueOn < :today")
    BigDecimal sumOverdue(@Param("today") LocalDate today);
}
