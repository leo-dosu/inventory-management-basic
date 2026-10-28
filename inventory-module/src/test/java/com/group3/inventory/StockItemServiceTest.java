package com.group3.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockItemServiceTest {

    @Mock
    private StockItemRepository repository;

    @InjectMocks
    private StockItemService stockItemService;

    private StockItem testStock;

    @BeforeEach
    void setUp() {

        testStock = new StockItem(
                "#1001",
                "Laptop",
                "Electronics",
                "Online Store",
                "New stock",
                20,
                "Pending"
        );
    }

    // =========================================
    // TEST 1 — GET ALL STOCK ITEMS
    // =========================================

    @Test
    void shouldGetAllStockItems() {

        StockItem secondStock = new StockItem(
                "#1002",
                "Mouse",
                "Accessories",
                "Physical Store",
                "New stock",
                50,
                "Completed"
        );

        when(repository.findAll())
                .thenReturn(List.of(testStock, secondStock));

        List<StockItem> result =
                stockItemService.getAllStockItems();

        assertEquals(2, result.size());

        assertEquals(
                "Laptop",
                result.get(0).getProduct()
        );

        assertEquals(
                "Mouse",
                result.get(1).getProduct()
        );

        verify(repository, times(1)).findAll();
    }

    // =========================================
    // TEST 2 — SAVE NEW STOCK
    // =========================================

    @Test
    void shouldSaveNewStockItem() {

        when(repository.save(testStock))
                .thenReturn(testStock);

        StockItem savedStock =
                stockItemService.saveStockItem(testStock);

        assertNotNull(savedStock);

        assertEquals(
                "Laptop",
                savedStock.getProduct()
        );

        assertEquals(
                20,
                savedStock.getItems()
        );

        verify(repository, times(1))
                .save(testStock);
    }

    // =========================================
    // TEST 3 — UPDATE EXISTING STOCK
    // =========================================

    @Test
    void shouldUpdateExistingStockItem() {

        testStock.setId(1L);

        testStock.setProduct("Gaming Laptop");
        testStock.setItems(35);
        testStock.setStatus("Completed");

        when(repository.save(testStock))
                .thenReturn(testStock);

        StockItem updatedStock =
                stockItemService.saveStockItem(testStock);

        assertEquals(
                1L,
                updatedStock.getId()
        );

        assertEquals(
                "Gaming Laptop",
                updatedStock.getProduct()
        );

        assertEquals(
                35,
                updatedStock.getItems()
        );

        assertEquals(
                "Completed",
                updatedStock.getStatus()
        );

        verify(repository, times(1))
                .save(testStock);
    }

    // =========================================
    // TEST 4 — DELETE STOCK
    // =========================================

    @Test
    void shouldDeleteStockItem() {

        Long stockId = 1L;

        stockItemService.deleteStockItem(stockId);

        verify(repository, times(1))
                .deleteById(stockId);
    }

    // =========================================
    // TEST 5 — COUNT STOCK ITEMS
    // =========================================

    @Test
    void shouldCountStockItems() {

        when(repository.count())
                .thenReturn(5L);

        long total =
                stockItemService.countStockItems();

        assertEquals(5L, total);

        verify(repository, times(1))
                .count();
    }
}