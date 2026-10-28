package com.group3.inventory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockItemService {

    private final StockItemRepository repository;

    public StockItemService(StockItemRepository repository) {
        this.repository = repository;
    }

    // Retrieve all inventory records
    public List<StockItem> getAllStockItems() {
        return repository.findAll();
    }

    // Save a new record or update an existing one
    public StockItem saveStockItem(StockItem stockItem) {
        return repository.save(stockItem);
    }

    // Delete an inventory record
    public void deleteStockItem(Long id) {
        repository.deleteById(id);
    }

    // Count all inventory records
    public long countStockItems() {
        return repository.count();
    }
}