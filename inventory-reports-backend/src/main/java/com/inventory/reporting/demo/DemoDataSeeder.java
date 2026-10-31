package com.inventory.reporting.demo;

import com.inventory.reporting.domain.Customer;
import com.inventory.reporting.domain.OrderStatus;
import com.inventory.reporting.domain.Product;
import com.inventory.reporting.domain.SalesOrder;
import com.inventory.reporting.repository.CustomerRepository;
import com.inventory.reporting.repository.ProductRepository;
import com.inventory.reporting.repository.SalesOrderRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/** Fills an empty database with two weeks of believable data so every report has something to show. */
@Component
@Profile("dev")
public class DemoDataSeeder implements ApplicationRunner {

    private final ProductRepository products;
    private final CustomerRepository customers;
    private final SalesOrderRepository orders;
    private final Clock clock;

    public DemoDataSeeder(ProductRepository products, CustomerRepository customers, SalesOrderRepository orders,
                          Clock clock) {
        this.products = products;
        this.customers = customers;
        this.orders = orders;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (products.count() > 0) {
            return;
        }
        List<Product> catalogue = products.saveAll(List.of(
                new Product("SKU-1001", "Wireless Mouse", "Accessories", new BigDecimal("4500.00")),
                new Product("SKU-1002", "Mechanical Keyboard", "Accessories", new BigDecimal("18500.00")),
                new Product("SKU-1003", "USB-C Charger", "Electronics", new BigDecimal("7200.00")),
                new Product("SKU-1004", "Laptop Stand", "Accessories", new BigDecimal("9800.00")),
                new Product("SKU-1005", "HDMI Cable", "Electronics", new BigDecimal("2500.00")),
                new Product("SKU-1006", "Webcam HD", "Electronics", new BigDecimal("15000.00")),
                new Product("SKU-1007", "Notebook A5", "Stationery", new BigDecimal("1200.00")),
                new Product("SKU-1008", "Desk Lamp", "Home", new BigDecimal("6400.00"))));

        List<Customer> people = customers.saveAll(List.of(
                new Customer("Ramesh Chaudhary"), new Customer("Amina Bello"), new Customer("Chidi Okafor"),
                new Customer("Funke Adeyemi"), new Customer("Tunde Balogun"), new Customer("Ngozi Eze")));

        LocalDate today = LocalDate.now(clock);
        List<String> channels = List.of("Store name", "Website", "Marketplace");
        List<String> destinations = List.of("Lagos", "Abuja", "Ibadan", "Lalitpur");
        Random random = new Random(42);
        int number = 1;

        for (int back = 13; back >= 0; back--) {
            LocalDate day = today.minusDays(back);
            int ordersThatDay = 3 + random.nextInt(6);
            for (int n = 0; n < ordersThatDay; n++) {
                int roll = random.nextInt(10);
                OrderStatus status = roll < 7 ? OrderStatus.COMPLETED : roll < 9 ? OrderStatus.PENDING : OrderStatus.RETURNED;
                SalesOrder order = new SalesOrder(String.format("ORD-%04d", number++), day,
                        people.get(random.nextInt(people.size())),
                        channels.get(random.nextInt(channels.size())),
                        destinations.get(random.nextInt(destinations.size())), status);
                int lines = 1 + random.nextInt(3);
                for (int l = 0; l < lines; l++) {
                    order.addItem(catalogue.get(random.nextInt(catalogue.size())), 1 + random.nextInt(4));
                }
                orders.save(order);
            }
        }
    }
}
