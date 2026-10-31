package com.group3.inventory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** Adds the five sample rows from the Figma design, only when the table is empty. */
@Component
public class StockDataInitializer implements CommandLineRunner {

    private final StockItemRepository repository;

    public StockDataInitializer(StockItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                new StockItem("#7676", "Inverter", "Category 1", "Store name", "Stock adjustment", 80, "Completed"),
                new StockItem("#7677", "Battery", "Category 1", "Store name", "", 80, "Pending"),
                new StockItem("#7678", "Generator", "Category 2", "Store name", "Stock adjustment", 80, "Completed"),
                new StockItem("#7679", "Charger", "Category 3", "Store name", "Stock adjustment", 80, "Completed"),
                new StockItem("#7680", "Power", "Category 2", "Store name", "", 80, "Completed")));
        System.out.println("Inventory sample data added.");
    }
}
