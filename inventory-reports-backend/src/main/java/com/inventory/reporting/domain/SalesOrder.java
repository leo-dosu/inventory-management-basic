package com.inventory.reporting.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_orders")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private LocalDate orderDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    private String salesChannel;

    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected SalesOrder() {
    }

    public SalesOrder(String orderNumber, LocalDate orderDate, Customer customer, String salesChannel,
                      String destination, OrderStatus status) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.customer = customer;
        this.salesChannel = salesChannel;
        this.destination = destination;
        this.status = status;
    }

    public OrderItem addItem(Product product, int quantity) {
        OrderItem item = new OrderItem(this, product, quantity, product.getUnitPrice());
        items.add(item);
        return item;
    }

    public BigDecimal total() {
        return items.stream().map(OrderItem::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int unitCount() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public Long getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public LocalDate getOrderDate() { return orderDate; }
    public Customer getCustomer() { return customer; }
    public String getSalesChannel() { return salesChannel; }
    public String getDestination() { return destination; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { return items; }

    public void setStatus(OrderStatus status) { this.status = status; }
}
