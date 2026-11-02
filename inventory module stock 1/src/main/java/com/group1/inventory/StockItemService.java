package com.group3.inventory;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

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

    // Search and filter in the database (null, blank or "All Status" means no filter)
    public List<StockItem> searchStockItems(String category, String status, LocalDate date, String text) {
        String like = (text == null || text.isBlank())
                ? null : "%" + text.trim().toLowerCase(Locale.ROOT) + "%";
        String cat = (category == null || category.isBlank()) ? null : category;
        String st = (status == null || status.isBlank() || "All Status".equals(status)) ? null : status;
        return repository.search(cat, st, date, like);
    }
}
