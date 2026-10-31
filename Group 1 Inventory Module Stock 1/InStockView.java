package com.group3.inventory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("In Stock | Inventory Management")
@PermitAll
public class InStockView extends VerticalLayout {

    private static final List<String> CATEGORIES = List.of("Category 1", "Category 2", "Category 3");
    private static final List<String> STATUSES = List.of("Completed", "Pending");

    private final StockItemService stockItemService;

    private final Tabs tabs = new Tabs(new Tab("All"), new Tab(CATEGORIES.get(0)),
            new Tab(CATEGORIES.get(1)), new Tab(CATEGORIES.get(2)));
    private final TextField search = new TextField();
    private final DatePicker dateFilter = new DatePicker();
    private final Select<String> statusFilter = new Select<>();
    private final Grid<StockItem> grid = new Grid<>(StockItem.class, false);
    private final Span emptyMsg = new Span("No stock matches these filters.");
    private final Button deleteSelected = new Button("Delete Selected");

    public InStockView(StockItemService stockItemService) {
        this.stockItemService = stockItemService;
        setSizeFull();
        setSpacing(false);

        // CATEGORY TABS
        tabs.addClassName("cat-tabs");
        tabs.addSelectedChangeListener(e -> refreshGrid());

        // TITLE ROW
        H2 title = new H2("In stock");
        title.addClassName("stock-title");
        deleteSelected.addClassName("btn-red");
        deleteSelected.setVisible(false);      // shown only when rows are selected
        deleteSelected.addClickListener(e -> confirmDelete());
        Button newStock = new Button("New Stock", VaadinIcon.PLUS.create(), e -> openNewStockDialog());
        newStock.addClassName("btn-blue");
        HorizontalLayout titleRow = new HorizontalLayout(title, new HorizontalLayout(deleteSelected, newStock));
        titleRow.setWidthFull();
        titleRow.setAlignItems(FlexComponent.Alignment.CENTER);
        titleRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // SEARCH, CALENDAR AND STATUS FILTER
        search.setPlaceholder("Quick search");
        search.setPrefixComponent(VaadinIcon.SEARCH.create());
        search.setValueChangeMode(ValueChangeMode.LAZY);
        search.addValueChangeListener(e -> refreshGrid());
        dateFilter.setPlaceholder("Date");
        dateFilter.addValueChangeListener(e -> refreshGrid());
        statusFilter.setPlaceholder("Status");
        statusFilter.setItems(STATUSES);
        statusFilter.setEmptySelectionAllowed(true);
        statusFilter.addValueChangeListener(e -> refreshGrid());
        HorizontalLayout filterRow = new HorizontalLayout(search, new HorizontalLayout(dateFilter, statusFilter));
        filterRow.setWidthFull();
        filterRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // TABLE
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(e -> deleteSelected.setVisible(!grid.getSelectedItems().isEmpty()));
        grid.addColumn(StockItem::getOrderId).setHeader("order ID").setAutoWidth(true);
        grid.addColumn(StockItem::getProduct).setHeader("Product").setAutoWidth(true);
        grid.addColumn(StockItem::getCategory).setHeader("Category").setAutoWidth(true);
        grid.addColumn(StockItem::getSalesChannel).setHeader("Sales channel").setAutoWidth(true);
        grid.addColumn(StockItem::getInstruction).setHeader("Instruction").setAutoWidth(true);
        grid.addColumn(i -> i.getItems() + "/" + i.getCapacity()).setHeader("Items").setAutoWidth(true);
        grid.addComponentColumn(this::statusSelect).setHeader("Status").setAutoWidth(true);
        grid.setSizeFull();
        emptyMsg.addClassName("empty-msg");
        emptyMsg.setVisible(false);

        // WHITE CARD UNDER THE TABS
        VerticalLayout card = new VerticalLayout(titleRow, filterRow, grid, emptyMsg);
        card.addClassName("stock-card");
        card.setSizeFull();
        add(tabs, card);
        expand(card);

        refreshGrid();
    }

    // STATUS DROPDOWN INSIDE EACH ROW
    private Select<String> statusSelect(StockItem item) {
        Select<String> select = new Select<>();
        select.setItems(STATUSES);
        select.setValue("Completed".equals(item.getStatus()) ? "Completed" : "Pending");
        select.setWidth("9em");
        select.addClassName("st-" + select.getValue());
        select.addValueChangeListener(e -> {
            if (!e.isFromClient()) {
                return;
            }
            try {
                item.setStatus(e.getValue());
                stockItemService.saveStockItem(item);
                refreshGrid();
            } catch (Exception ex) {
                error("Unable to update status.");
            }
        });
        return select;
    }

    // LOAD DATA FROM MYSQL WITH THE CURRENT FILTERS
    private void refreshGrid() {
        int tab = tabs.getSelectedIndex();
        List<StockItem> rows = stockItemService.searchStockItems(
                tab <= 0 ? null : CATEGORIES.get(tab - 1),
                statusFilter.getValue(), dateFilter.getValue(), search.getValue());
        grid.setItems(rows);
        emptyMsg.setVisible(rows.isEmpty());
    }

    private void confirmDelete() {
        var selected = List.copyOf(grid.getSelectedItems());
        if (selected.isEmpty()) {
            return;
        }
        ConfirmDialog confirm = new ConfirmDialog();
        confirm.setHeader("Delete Stock");
        confirm.setText("Are you sure you want to permanently delete " + selected.size()
                + " selected product(s)? This action cannot be undone.");
        confirm.setCancelable(true);
        confirm.setConfirmText("Delete");
        confirm.setConfirmButtonTheme("error primary");
        confirm.addConfirmListener(e -> {
            try {
                selected.forEach(i -> stockItemService.deleteStockItem(i.getId()));
                grid.deselectAll();
                refreshGrid();
                success("Selected stock deleted successfully!");
            } catch (Exception ex) {
                error("Unable to delete selected stock.");
            }
        });
        confirm.open();
    }

    private void openNewStockDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("550px");
        dialog.setMaxWidth("95vw");
        H3 formTitle = new H3("Add New Stock");
        formTitle.addClassName("stock-title");

        TextField orderId = new TextField("Order ID");
        orderId.setPlaceholder("e.g. #7676");
        orderId.setRequiredIndicatorVisible(true);
        TextField product = new TextField("Product");
        product.setPlaceholder("e.g. Inverter");
        product.setRequiredIndicatorVisible(true);
        Select<String> category = new Select<>();
        category.setLabel("Category");
        category.setItems(CATEGORIES);
        category.setValue(CATEGORIES.get(0));
        TextField salesChannel = new TextField("Sales Channel");
        salesChannel.setValue("Store name");
        TextField instruction = new TextField("Instruction");
        instruction.setPlaceholder("e.g. Stock adjustment");
        IntegerField items = new IntegerField("Items");
        items.setMin(0);
        items.setStepButtonsVisible(true);
        items.setValue(0);
        IntegerField capacity = new IntegerField("Capacity");
        capacity.setMin(1);
        capacity.setStepButtonsVisible(true);
        capacity.setValue(100);
        Select<String> status = new Select<>();
        status.setLabel("Status");
        status.setItems(STATUSES);
        status.setValue("Pending");

        FormLayout form = new FormLayout(orderId, product, category, salesChannel,
                instruction, items, capacity, status);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("450px", 2));

        Button cancel = new Button("Cancel", e -> dialog.close());
        Button save = new Button("Save Stock");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> {
            if (orderId.isEmpty() || product.isEmpty()) {
                Notification.show("Please enter Order ID and Product", 3000, Notification.Position.MIDDLE);
                return;
            }
            Integer quantity = items.getValue();
            Integer cap = capacity.getValue();
            if (quantity == null || quantity < 0 || cap == null || cap < 1) {
                Notification.show("Please enter a valid quantity", 3000, Notification.Position.MIDDLE);
                return;
            }
            StockItem item = new StockItem(orderId.getValue().trim(), product.getValue().trim(),
                    category.getValue(), salesChannel.getValue(), instruction.getValue(),
                    quantity, status.getValue());
            item.setCapacity(cap);
            try {
                stockItemService.saveStockItem(item);
                refreshGrid();
                dialog.close();
                success("Stock saved successfully!");
            } catch (Exception ex) {
                error("Unable to save stock. Please try again.");
            }
        });

        HorizontalLayout buttons = new HorizontalLayout(cancel, save);
        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        dialog.add(new VerticalLayout(formTitle, form, buttons));
        dialog.open();
    }

    private void success(String message) {
        Notification.show(message, 3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void error(String message) {
        Notification.show(message, 5000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
