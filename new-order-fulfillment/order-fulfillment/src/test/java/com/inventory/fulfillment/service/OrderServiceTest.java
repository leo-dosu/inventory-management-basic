package com.inventory.fulfillment.service;
import com.inventory.fulfillment.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class OrderServiceTest {
@Test void rejectsInvalidQuantity(){OrderService s=new OrderService(mock(OrderRepository.class));assertThrows(IllegalArgumentException.class,()->s.create("Ada","Lagos","Laptop",0));}
@Test void rejectsMissingCustomer(){OrderService s=new OrderService(mock(OrderRepository.class));assertThrows(IllegalArgumentException.class,()->s.create("","Lagos","Laptop",2));}
}
