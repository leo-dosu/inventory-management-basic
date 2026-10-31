package com.inventory.reporting.repository;

import com.inventory.reporting.api.dto.TopSellingProduct;
import com.inventory.reporting.domain.OrderItem;
import com.inventory.reporting.domain.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select new com.inventory.reporting.api.dto.TopSellingProduct(p.id, p.sku, p.name, sum(i.quantity), "
            + "sum(i.unitPrice * i.quantity)) "
            + "from OrderItem i join i.product p join i.order o "
            + "where o.status = :status "
            + "group by p.id, p.sku, p.name "
            + "order by sum(i.quantity) desc, p.name asc")
    List<TopSellingProduct> findTopSelling(@Param("status") OrderStatus status, Pageable pageable);
}
