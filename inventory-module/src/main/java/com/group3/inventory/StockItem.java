package com.group3.inventory;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stock_items")
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String product;

    private String category;

    @Column(name = "sales_channel")
    private String salesChannel;

    private String instruction;

    private int items;

    private String status;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    public StockItem() {
    }

    public StockItem(
            String orderId,
            String product,
            String category,
            String salesChannel,
            String instruction,
            int items,
            String status
    ) {
        this.orderId = orderId;
        this.product = product;
        this.category = category;
        this.salesChannel = salesChannel;
        this.instruction = instruction;
        this.items = items;
        this.status = status;
        this.dateAdded = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @PrePersist
    public void onCreate() {
        if (dateAdded == null) {
            dateAdded = LocalDate.now();
        }
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setId(Long id) {
        this.id = id;
    }
}