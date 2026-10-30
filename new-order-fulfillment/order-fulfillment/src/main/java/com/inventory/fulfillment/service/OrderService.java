package com.inventory.fulfillment.service;
import com.inventory.fulfillment.domain.*;
import com.inventory.fulfillment.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service public class OrderService {
private final OrderRepository repo;
public OrderService(OrderRepository repo){this.repo=repo;}
public List<CustomerOrder> all(){return repo.findAll();}
public CustomerOrder create(String customer,String destination,String product,int quantity){if(customer==null||customer.isBlank()||destination==null||destination.isBlank()||product==null||product.isBlank()||quantity<1)throw new IllegalArgumentException("Provide customer, destination, product and a positive quantity");CustomerOrder o=new CustomerOrder();o.setCustomer(customer.trim());o.setDestination(destination.trim());o.setProduct(product.trim());o.setQuantity(quantity);return repo.save(o);}
@Transactional public CustomerOrder update(Long id,OrderStatus status,String courier,String tracking){CustomerOrder o=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Order not found"));if(o.getStatus()==OrderStatus.CANCELLED||o.getStatus()==OrderStatus.DELIVERED)throw new IllegalStateException("Completed orders cannot be changed");if(status==OrderStatus.SHIPPED&&(tracking==null||tracking.isBlank()||courier==null||courier.isBlank()))throw new IllegalArgumentException("Courier and tracking number required for shipping");o.setStatus(status);o.setCourier(courier);o.setTrackingNumber(tracking);return repo.save(o);}
}
