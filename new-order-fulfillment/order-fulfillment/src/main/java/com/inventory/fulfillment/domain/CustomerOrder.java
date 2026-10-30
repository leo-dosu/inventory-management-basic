package com.inventory.fulfillment.domain;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="customer_orders") public class CustomerOrder {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
@Column(nullable=false) private String customer;
@Column(nullable=false) private String destination;
private String salesChannel="Store";
private String product;
private int quantity;
private String trackingNumber;
private String courier;
@Enumerated(EnumType.STRING) private OrderStatus status=OrderStatus.PENDING;
private LocalDateTime createdAt=LocalDateTime.now();
public Long getId(){return id;} public String getCustomer(){return customer;} public void setCustomer(String v){customer=v;} public String getDestination(){return destination;} public void setDestination(String v){destination=v;} public String getSalesChannel(){return salesChannel;} public void setSalesChannel(String v){salesChannel=v;} public String getProduct(){return product;} public void setProduct(String v){product=v;} public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;} public String getTrackingNumber(){return trackingNumber;} public void setTrackingNumber(String v){trackingNumber=v;} public String getCourier(){return courier;} public void setCourier(String v){courier=v;} public OrderStatus getStatus(){return status;} public void setStatus(OrderStatus v){status=v;} public LocalDateTime getCreatedAt(){return createdAt;}
}
