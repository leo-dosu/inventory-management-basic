package com.inventory.pos.demo;

import com.inventory.pos.core.domain.Customer;
import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.PaymentMethod;
import com.inventory.pos.core.domain.Product;
import com.inventory.pos.core.repository.CustomerRepository;
import com.inventory.pos.core.repository.InvoiceRepository;
import com.inventory.pos.core.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/**
 * Fills an empty database with a small shop: a catalogue, four customers and five invoices. Dates are set relative to
 * today so there are always some overdue debts, a partly paid invoice and a fully paid one to look at.
 */
@Component
@Profile("dev")
public class DemoDataSeeder implements ApplicationRunner {

    private final ProductRepository products;
    private final CustomerRepository customers;
    private final InvoiceRepository invoices;
    private final Clock clock;
    private final BigDecimal taxRate;

    public DemoDataSeeder(ProductRepository products, CustomerRepository customers, InvoiceRepository invoices,
                          Clock clock, @Value("${pos.tax-rate}") BigDecimal taxRate) {
        this.products = products;
        this.customers = customers;
        this.invoices = invoices;
        this.clock = clock;
        this.taxRate = taxRate;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (products.count() > 0) {
            return;
        }
        List<Product> catalogue = products.saveAll(List.of(
                product("COF-ARA-01", "Premium Arabica Roast Coffee (1kg)", "Beverages", "2450", 45, "bag"),
                product("BEV-ALM-02", "Organic Almond Milk 1L", "Beverages", "480", 80, "carton"),
                product("BAK-SDR-03", "Artisan Sourdough Loaf", "Bakery", "625", 24, "loaf"),
                product("SUP-TAP-04", "Heavy Duty Packaging Tape (6-Pack)", "Supplies", "1800", 60, "pack"),
                product("SUP-BOX-05", "Recycled Kraft Shipping Boxes (25ct)", "Supplies", "3250", 35, "bundle"),
                product("POS-PAP-06", "Thermal Receipt Rolls (50 rolls)", "Supplies", "2999", 50, "box"),
                product("JAN-CLN-07", "Commercial Grade Floor Cleaner 5L", "Maintenance", "2200", 18, "jug"),
                product("JAN-CLT-08", "Microfiber Cleaning Cloths (Pack of 12)", "Maintenance", "1450", 40, "pack"),
                product("POS-LBL-09", "Direct Thermal Barcode Labels (1000/roll)", "Supplies", "1200", 95, "roll"),
                product("GRO-HON-10", "Pure Clover Honey 500g", "Groceries", "975", 30, "jar"),
                product("GRO-OIL-11", "Extra Virgin Olive Oil 1L Cold Pressed", "Groceries", "1650", 28, "bottle"),
                product("SUP-MRK-12", "Industrial Sharp Permanent Markers (Box of 12)", "Supplies", "1120", 65, "box")));
        Product tape = catalogue.get(3), boxes = catalogue.get(4), rolls = catalogue.get(5);
        Product cleaner = catalogue.get(6), cloths = catalogue.get(7), labels = catalogue.get(8);
        Product honey = catalogue.get(9), oil = catalogue.get(10);

        Customer apex = customers.save(new Customer("Apex Logistics & Freight", "+234 800 555 0101",
                "billing@apexlogistics.com", "14 Industrial Way, Dock 4, Bay City",
                "Long-term corporate client. Bi-weekly bulk order.", 14));
        Customer sarah = customers.save(new Customer("Sarah Jenkins (Harbor Cafe)", "+234 800 555 0102",
                "sarah@harborcafe.net", "88 Boardwalk Promenade, Seaside", "Cafe proprietor. Net 14 terms.", 14));
        Customer vance = customers.save(new Customer("Vance Auto Restoration", "+234 800 555 0103",
                "marcus@vanceauto.org", "320 Garage Row, East District",
                "Contact Marcus Vance directly. Follow up on overdue accounts.", 14));
        Customer greenfield = customers.save(new Customer("Greenfield Community School", "+234 800 555 0104",
                "procurement@greenfieldschool.edu", "500 Elmwood Avenue, North Suburbs",
                "District purchase order client. Net 30 terms.", 30));

        LocalDate today = LocalDate.now(clock);

        Invoice vanceInvoice = invoice(1, vance, today.minusDays(30), today.minusDays(16));
        vanceInvoice.addLine(cleaner.getId(), cleaner.getSku(), cleaner.getName(), 3, cleaner.getUnitPrice());
        vanceInvoice.addLine(cloths.getId(), cloths.getSku(), cloths.getName(), 6, cloths.getUnitPrice());
        vanceInvoice.applyDiscount(BigDecimal.ZERO);
        vanceInvoice.addPayment(new BigDecimal("5000.00"), today.minusDays(25), PaymentMethod.TRANSFER, "Deposit");

        Invoice sarahInvoice = invoice(2, sarah, today.minusDays(21), today.minusDays(7));
        sarahInvoice.addLine(tape.getId(), tape.getSku(), tape.getName(), 8, tape.getUnitPrice());
        sarahInvoice.addLine(labels.getId(), labels.getSku(), labels.getName(), 10, labels.getUnitPrice());
        sarahInvoice.applyDiscount(BigDecimal.ZERO);

        Invoice apexInvoice = invoice(3, apex, today.minusDays(14), today);
        apexInvoice.addLine(boxes.getId(), boxes.getSku(), boxes.getName(), 15, boxes.getUnitPrice());
        apexInvoice.addLine(rolls.getId(), rolls.getSku(), rolls.getName(), 12, rolls.getUnitPrice());
        apexInvoice.applyDiscount(new BigDecimal("2500.00"));
        apexInvoice.addPayment(new BigDecimal("40000.00"), today.minusDays(10), PaymentMethod.TRANSFER, "Part payment");

        Invoice greenfieldInvoice = invoice(4, greenfield, today.minusDays(20), today.plusDays(10));
        greenfieldInvoice.addLine(tape.getId(), tape.getSku(), tape.getName(), 4, tape.getUnitPrice());
        greenfieldInvoice.addLine(cloths.getId(), cloths.getSku(), cloths.getName(), 10, cloths.getUnitPrice());
        greenfieldInvoice.applyDiscount(BigDecimal.ZERO);
        greenfieldInvoice.addPayment(greenfieldInvoice.getTotal(), today.minusDays(8), PaymentMethod.TRANSFER,
                "Paid in full");

        Invoice walkIn = invoice(5, null, today, today);
        walkIn.addLine(honey.getId(), honey.getSku(), honey.getName(), 1, honey.getUnitPrice());
        walkIn.addLine(oil.getId(), oil.getSku(), oil.getName(), 2, oil.getUnitPrice());
        walkIn.applyDiscount(BigDecimal.ZERO);
        walkIn.addPayment(walkIn.getTotal(), today, PaymentMethod.CASH, "Paid at the register");

        invoices.saveAll(List.of(vanceInvoice, sarahInvoice, apexInvoice, greenfieldInvoice, walkIn));
    }

    private Invoice invoice(int sequence, Customer customer, LocalDate issued, LocalDate due) {
        return new Invoice(String.format("INV-%d-%03d", issued.getYear(), sequence), customer, issued, due, taxRate);
    }

    private static Product product(String sku, String name, String category, String price, int stock, String unit) {
        return new Product(sku, name, category, new BigDecimal(price).setScale(2), stock, unit);
    }
}
