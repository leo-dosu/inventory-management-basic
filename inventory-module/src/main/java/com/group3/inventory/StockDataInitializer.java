package com.group3.inventory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// @component

public class StockDataInitializer implements CommandLineRunner {

    private final StockItemRepository repository;

    public StockDataInitializer(StockItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        addStockIfMissing(
                "#7676",
                "Inverter",
                "cat1",
                "Store name",
                "Stock adjustment",
                80,
                "Completed"
        );

        addStockIfMissing(
                "#7677",
                "Battery",
                "cat2",
                "Store name",
                "",
                80,
                "Pending"
        );

        addStockIfMissing(
                "#7678",
                "Generator",
                "cat2",
                "Store name",
                "Stock adjustment",
                80,
                "Completed"
        );

        addStockIfMissing(
                "#7679",
                "Charger",
                "cat3",
                "Store name",
                "Stock adjustment",
                80,
                "Completed"
        );

        addStockIfMissing(
                "#7680",
                "Power",
                "cat4",
                "Store name",
                "",
                80,
                "Completed"
        );

        System.out.println(
                "Inventory sample data initialization completed!"
        );
    }

    private void addStockIfMissing(
            String orderId,
            String product,
            String category,
            String salesChannel,
            String instruction,
            int items,
            String status
    ) {

        // Check whether this order already exists.
        boolean exists = repository.findAll()
                .stream()
                .anyMatch(stock ->
                        orderId.equals(stock.getOrderId())
                );

        // Only insert missing products.
        if (!exists) {

            StockItem stockItem = new StockItem(
                    orderId,
                    product,
                    category,
                    salesChannel,
                    instruction,
                    items,
                    status
            );

            repository.save(stockItem);

            System.out.println(
                    "Added sample product: " + product
            );
        }
    }
}