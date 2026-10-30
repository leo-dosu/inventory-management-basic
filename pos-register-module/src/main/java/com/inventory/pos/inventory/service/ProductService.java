package com.inventory.pos.inventory.service;

import com.inventory.pos.core.BusinessException;
import com.inventory.pos.core.domain.Product;
import com.inventory.pos.core.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository products;

    public ProductService(ProductRepository products) {
        this.products = products;
    }

    /** Products matching the search text (name or SKU) and, if given, the category. */
    public List<Product> list(String search, String category) {
        String query = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
        return products.findAllByOrderByIdAsc().stream()
                .filter(p -> category == null || category.equals(p.getCategory()))
                .filter(p -> query.isEmpty()
                        || p.getName().toLowerCase(Locale.ROOT).contains(query)
                        || p.getSku().toLowerCase(Locale.ROOT).contains(query))
                .toList();
    }

    public List<String> categories() {
        return products.findAll().stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull)
                .filter(c -> !c.isBlank())
                .distinct()
                .sorted()
                .toList();
    }

    @Transactional
    public Product save(ProductForm form) {
        String sku = form.sku() == null ? "" : form.sku().trim();
        String name = form.name() == null ? "" : form.name().trim();
        if (sku.isEmpty()) {
            throw new BusinessException("Enter a SKU.");
        }
        if (name.isEmpty()) {
            throw new BusinessException("Enter a product name.");
        }
        if (form.unitPrice() == null || form.unitPrice().signum() < 0) {
            throw new BusinessException("Enter a price of zero or more.");
        }
        if (form.stock() < 0) {
            throw new BusinessException("Stock cannot be negative.");
        }
        products.findBySku(sku)
                .filter(existing -> !existing.getId().equals(form.id()))
                .ifPresent(existing -> {
                    throw new BusinessException("Another product already uses the SKU " + sku + ".");
                });

        Product product = form.id() == null
                ? new Product(sku, name, blankToNull(form.category()), form.unitPrice(),
                        form.stock(), blankToNull(form.unit()))
                : products.findById(form.id()).orElseThrow(() -> new BusinessException("Product not found."));
        product.setSku(sku);
        product.setName(name);
        product.setCategory(blankToNull(form.category()));
        product.setUnitPrice(form.unitPrice().setScale(2, RoundingMode.HALF_UP));
        product.setStock(form.stock());
        product.setUnit(blankToNull(form.unit()));
        return products.save(product);
    }

    @Transactional
    public void delete(Long id) {
        if (!products.existsById(id)) {
            throw new BusinessException("Product not found.");
        }
        products.deleteById(id);
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
