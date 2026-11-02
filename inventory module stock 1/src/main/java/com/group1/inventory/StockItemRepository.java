package com.group3.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    @Query("""
            select s from StockItem s
            where (:category is null or s.category = :category)
              and (:status is null or s.status = :status)
              and (:date is null or s.dateAdded = :date)
              and (:text is null or lower(s.orderId) like :text or lower(s.product) like :text
                   or lower(s.category) like :text or lower(s.salesChannel) like :text)
            order by s.id desc""")
    List<StockItem> search(@Param("category") String category,
                           @Param("status") String status,
                           @Param("date") LocalDate date,
                           @Param("text") String text);
}
