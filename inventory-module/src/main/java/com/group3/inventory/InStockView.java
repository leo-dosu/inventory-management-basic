package com.group3.inventory;

import java.time.LocalDate;
import java.util.Set;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Locale;

@Route("")
@PageTitle("In Stock | Inventory Management")
@PermitAll
public class InStockView extends VerticalLayout {

    // DATABASE SERVICE
    private final StockItemService stockItemService;

    // INVENTORY TABLE
    private final Grid<StockItem> grid =
            new Grid<>(StockItem.class, false);

    // SEARCH AND FILTER COMPONENTS
    private final TextField search = new TextField();
    private final Select<String> statusFilter = new Select<>();
    private final DatePicker dateFilter = new DatePicker();

    // CONSTRUCTOR
    public InStockView(StockItemService stockItemService) {

        this.stockItemService = stockItemService;

        // =========================
        // MAIN PAGE
        // =========================

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        getStyle()
                .set("background", "#f3f6f9")
                .set("font-family", "Arial, sans-serif");

        // =========================
        // TOP NAVIGATION
        // =========================

        HorizontalLayout topBar = new HorizontalLayout();

        topBar.setWidthFull();
        topBar.setHeight("65px");
        topBar.setAlignItems(Alignment.CENTER);
        topBar.setJustifyContentMode(
                JustifyContentMode.BETWEEN
        );

        topBar.getStyle()
                .set("background", "white")
                .set("padding", "0 25px")
                .set("border-bottom", "1px solid #e5e7eb");

        Button menuButton = new Button(
                new Icon(VaadinIcon.MENU)
        );

        menuButton.getStyle()
                .set("background", "transparent")
                .set("color", "#333");

        HorizontalLayout profile = new HorizontalLayout();

        Icon searchIcon = new Icon(VaadinIcon.SEARCH);
        Icon bellIcon = new Icon(VaadinIcon.BELL);

        Span username = new Span("Ann Lee");

        profile.add(
                searchIcon,
                bellIcon,
                username
        );

        profile.setAlignItems(Alignment.CENTER);
        profile.setSpacing(true);

        topBar.add(menuButton, profile);

        // =========================
        // BODY
        // =========================

        HorizontalLayout body = new HorizontalLayout();

        body.setSizeFull();
        body.setPadding(false);
        body.setSpacing(false);

        // =========================
        // BLUE SIDEBAR
        // =========================

        VerticalLayout sidebar = new VerticalLayout();

        sidebar.setWidth("85px");
        sidebar.setMinWidth("85px");
        sidebar.setHeightFull();
        sidebar.setPadding(false);
        sidebar.setSpacing(false);
        sidebar.setAlignItems(Alignment.CENTER);

        sidebar.getStyle()
                .set("background", "#1687d9")
                .set("padding-top", "20px");

        sidebar.add(
                sidebarButton(
                        VaadinIcon.DASHBOARD,
                        "Dashboard"
                ),
                sidebarButton(
                        VaadinIcon.LIST,
                        "Stocks"
                ),
                sidebarButton(
                        VaadinIcon.PACKAGE,
                        "Products"
                ),
                sidebarButton(
                        VaadinIcon.CART,
                        "Sales"
                ),
                sidebarButton(
                        VaadinIcon.CLIPBOARD,
                        "Orders"
                ),
                sidebarButton(
                        VaadinIcon.USERS,
                        "Users"
                )
        );

        // =========================
        // CONTENT AREA
        // =========================

        VerticalLayout content = new VerticalLayout();

        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);

        content.getStyle()
                .set("background", "white")
                .set("margin", "15px")
                .set("padding", "20px");

        // =========================
        // PAGE TITLE
        // =========================

        H2 title = new H2("In stock");

        title.getStyle()
                .set("color", "#2785b4")
                .set("font-size", "23px")
                .set("font-weight", "500")
                .set("margin", "0");

        // DELETE SELECTED BUTTON
        Button deleteSelected = new Button("Delete Selected");

        deleteSelected.getStyle()
                .set("background", "#dc3545")
                .set("color", "white")
                .set("border-radius", "3px")
                .set("cursor", "pointer");

// HIDE BUTTON UNTIL A PRODUCT IS SELECTED
        deleteSelected.setVisible(false);

        // NEW STOCK BUTTON
        Button newStock = new Button("+ New Stock");

        newStock.getStyle()
                .set("background", "#1687d9")
                .set("color", "white")
                .set("border-radius", "3px")
                .set("cursor", "pointer");

        // OPEN FORM WHEN BUTTON IS CLICKED
        newStock.addClickListener(event -> openNewStockDialog());

        HorizontalLayout actionButtons = new HorizontalLayout(
                deleteSelected,
                newStock
        );

        actionButtons.setAlignItems(Alignment.CENTER);

        HorizontalLayout titleRow = new HorizontalLayout(
                title,
                actionButtons
        );

        titleRow.setWidthFull();
        titleRow.setAlignItems(Alignment.CENTER);
        titleRow.setJustifyContentMode(
                JustifyContentMode.BETWEEN
        );

        // =========================
        // SEARCH BAR
        // =========================

        search.setPlaceholder("Quick search");

        search.setPrefixComponent(
                new Icon(VaadinIcon.SEARCH)
        );

        search.setWidth("230px");

        // SEARCH AS USER TYPES
        search.setValueChangeMode(
                com.vaadin.flow.data.value.ValueChangeMode.EAGER
        );

        search.addValueChangeListener(
                event -> refreshGrid()
        );

        // =========================
        // DATE FILTER
        // =========================


        dateFilter.setPlaceholder("Select date");
        dateFilter.setWidth("160px");

        // Date filtering will be added after
        // we introduce a date field in StockItem.
        dateFilter.setEnabled(true);

        dateFilter.setTooltipText(
                "Filter products by the date they were added"
        );

        dateFilter.addValueChangeListener(event -> refreshGrid());

        // =========================
        // STATUS FILTER
        // =========================

        statusFilter.setItems(
                "All Status",
                "Completed",
                "Pending"
        );

        statusFilter.setValue("All Status");
        statusFilter.setWidth("150px");

        statusFilter.addValueChangeListener(
                event -> refreshGrid()
        );

        HorizontalLayout filters = new HorizontalLayout(
                dateFilter,
                statusFilter
        );

        filters.setAlignItems(Alignment.CENTER);

        HorizontalLayout filterRow = new HorizontalLayout(
                search,
                filters
        );

        filterRow.setWidthFull();
        filterRow.setAlignItems(Alignment.CENTER);
        filterRow.setJustifyContentMode(
                JustifyContentMode.BETWEEN
        );

        // =========================
        // INVENTORY TABLE
        // =========================

        grid.setWidthFull();
        grid.setAllRowsVisible(true);

        grid.setSelectionMode(
                Grid.SelectionMode.MULTI
        );

        // SHOW DELETE BUTTON ONLY WHEN ITEMS ARE SELECTED
        grid.addSelectionListener(event -> {

            boolean hasSelection = !grid.getSelectedItems().isEmpty();

            deleteSelected.setVisible(hasSelection);
        });

        // ORDER ID
        grid.addColumn(StockItem::getOrderId)
                .setHeader("Order ID")
                .setAutoWidth(true);

        // PRODUCT
        grid.addColumn(StockItem::getProduct)
                .setHeader("Product")
                .setAutoWidth(true);

        // CATEGORY
        grid.addColumn(StockItem::getCategory)
                .setHeader("Category")
                .setAutoWidth(true);

        // SALES CHANNEL
        grid.addColumn(StockItem::getSalesChannel)
                .setHeader("Sales channel")
                .setAutoWidth(true);

        // INSTRUCTION
        grid.addColumn(StockItem::getInstruction)
                .setHeader("Instruction")
                .setAutoWidth(true);

        // ITEMS
        grid.addColumn(StockItem::getItems)
                .setHeader("Items")
                .setAutoWidth(true);

        // =========================
        // STATUS BADGES
        // =========================

        grid.addColumn(
                new ComponentRenderer<>(item -> {

                    String status = item.getStatus();

                    Span badge = new Span(
                            status == null ? "" : status
                    );

                    badge.getStyle()
                            .set("padding", "5px 15px")
                            .set("border-radius", "20px")
                            .set("font-size", "12px")
                            .set("display", "inline-block")
                            .set("min-width", "90px")
                            .set("text-align", "center");

                    if ("Completed".equals(status)) {

                        badge.getStyle()
                                .set("background", "#58bd94")
                                .set("color", "white");

                    } else {

                        badge.getStyle()
                                .set("background", "#c7e89b")
                                .set("color", "#385b20");
                    }

                    return badge;
                })
        ).setHeader("Status");

        // =========================================
// DELETE SELECTED STOCK
// =========================================

        deleteSelected.addClickListener(event -> {

            Set<StockItem> selectedItems = grid.getSelectedItems();

            if (selectedItems.isEmpty()) {
                return;
            }

            int selectedCount = selectedItems.size();

            ConfirmDialog confirmDialog = new ConfirmDialog();

            confirmDialog.setHeader("Delete Stock");

            confirmDialog.setText(
                    "Are you sure you want to permanently delete "
                            + selectedCount
                            + " selected product(s)? This action cannot be undone."
            );

            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("Cancel");

            confirmDialog.setConfirmText("Delete");

            confirmDialog.setConfirmButtonTheme("error primary");

            confirmDialog.addConfirmListener(confirmEvent -> {

                try {

                    // DELETE EACH SELECTED PRODUCT FROM MYSQL
                    for (StockItem stockItem : selectedItems) {
                        stockItemService.deleteStockItem(stockItem.getId());
                    }

                    // CLEAR TABLE SELECTION
                    grid.deselectAll();

                    // REFRESH TABLE FROM DATABASE
                    refreshGrid();

                    Notification notification = Notification.show(
                            "Selected stock deleted successfully!",
                            3000,
                            Notification.Position.TOP_CENTER
                    );

                    notification.addThemeVariants(
                            NotificationVariant.LUMO_SUCCESS
                    );

                } catch (Exception exception) {

                    Notification notification = Notification.show(
                            "Unable to delete selected stock.",
                            5000,
                            Notification.Position.MIDDLE
                    );

                    notification.addThemeVariants(
                            NotificationVariant.LUMO_ERROR
                    );
                }
            });

            confirmDialog.open();
        });

        // =========================
        // LOAD DATA FROM MYSQL
        // =========================

        // OPEN EDIT FORM WHEN A PRODUCT ROW IS CLICKED
        grid.addItemClickListener(event -> {
            StockItem selectedItem = event.getItem();
            openEditStockDialog(selectedItem);
        });
        refreshGrid();

        // =========================
        // ASSEMBLE PAGE
        // =========================

        content.add(
                titleRow,
                filterRow,
                grid
        );

        body.add(
                sidebar,
                content
        );

        body.setFlexGrow(1, content);

        add(
                topBar,
                body
        );

        expand(body);
    }

    // =========================================
    // REFRESH INVENTORY TABLE FROM MYSQL
    // =========================================

    private void refreshGrid() {

        // FETCH REAL DATA FROM DATABASE
        List<StockItem> items =
                stockItemService.getAllStockItems();

        // GET SEARCH TEXT
        String searchText = search.getValue()
                .trim()
                .toLowerCase(Locale.ROOT);

        // GET SELECTED STATUS
        String selectedStatus = statusFilter.getValue();
        LocalDate selectedDate = dateFilter.getValue();

        // APPLY SEARCH AND STATUS FILTERS
        List<StockItem> filteredItems = items.stream()

                .filter(item -> {

                    if (searchText.isEmpty()) {
                        return true;
                    }

                    String orderId = safeText(item.getOrderId());
                    String product = safeText(item.getProduct());
                    String category = safeText(item.getCategory());
                    String salesChannel = safeText(item.getSalesChannel());

                    return orderId.contains(searchText)
                            || product.contains(searchText)
                            || category.contains(searchText)
                            || salesChannel.contains(searchText);
                })

                .filter(item -> {

                    if (selectedStatus == null
                            || selectedStatus.equals("All Status")) {
                        return true;
                    }

                    return selectedStatus.equals(
                            item.getStatus()
                    );
                })

                .filter(item -> {

                    // Show all dates when no date is selected
                    if (selectedDate == null) {
                        return true;
                    }

                    // Match the selected date against Date Added
                    return selectedDate.equals(item.getDateAdded());
                })

                .toList();

        // UPDATE TABLE
        grid.setItems(filteredItems);
    }

    // =========================================
    // HANDLE NULL VALUES DURING SEARCH
    // =========================================

    private String safeText(String value) {

        return value == null
                ? ""
                : value.toLowerCase(Locale.ROOT);
    }

    // =========================================
    // NEW STOCK FORM
    // =========================================

    private void openNewStockDialog() {

        Dialog dialog = new Dialog();

        dialog.setWidth("550px");
        dialog.setMaxWidth("95vw");

        H3 formTitle = new H3("Add New Stock");

        formTitle.getStyle()
                .set("color", "#1687d9")
                .set("margin-top", "0");

        // ORDER ID FIELD
        TextField orderId = new TextField("Order ID");

        orderId.setPlaceholder("e.g. #7676");
        orderId.setRequiredIndicatorVisible(true);

        // PRODUCT FIELD
        TextField product = new TextField("Product");

        product.setPlaceholder("e.g. Inverter");
        product.setRequiredIndicatorVisible(true);

        // CATEGORY FIELD
        TextField category = new TextField("Category");

        category.setPlaceholder("e.g. Electronics");

        // SALES CHANNEL FIELD
        TextField salesChannel = new TextField("Sales Channel");

        salesChannel.setPlaceholder("e.g. Store name");

        // INSTRUCTION FIELD
        TextField instruction = new TextField("Instruction");

        instruction.setPlaceholder("e.g. Stock adjustment");

        // ITEMS FIELD
        IntegerField items = new IntegerField("Items");

        items.setPlaceholder("Enter quantity");
        items.setMin(0);
        items.setStepButtonsVisible(true);
        items.setValue(0);

        // STATUS FIELD
        Select<String> status = new Select<>();

        status.setLabel("Status");

        status.setItems(
                "Completed",
                "Pending"
        );

        status.setValue("Pending");

        // FORM LAYOUT
        FormLayout form = new FormLayout();

        form.add(
                orderId,
                product,
                category,
                salesChannel,
                instruction,
                items,
                status
        );

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("450px", 2)
        );

        // CANCEL BUTTON
        Button cancel = new Button(
                "Cancel",
                event -> dialog.close()
        );

        // SAVE BUTTON
        Button save = new Button("Save Stock");

        save.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        save.getStyle()
                .set("background", "#1687d9")
                .set("color", "white");

        // =====================================
        // SAVE STOCK TO MYSQL
        // =====================================

        save.addClickListener(event -> {

            // VALIDATE REQUIRED FIELDS
            if (orderId.isEmpty() || product.isEmpty()) {

                Notification.show(
                        "Please enter Order ID and Product",
                        3000,
                        Notification.Position.MIDDLE
                );

                return;
            }

            Integer quantity = items.getValue();

            if (quantity == null || quantity < 0) {

                Notification.show(
                        "Please enter a valid quantity",
                        3000,
                        Notification.Position.MIDDLE
                );

                return;
            }

            // CREATE DATABASE ENTITY
            StockItem newItem = new StockItem(
                    orderId.getValue().trim(),
                    product.getValue().trim(),
                    category.getValue(),
                    salesChannel.getValue(),
                    instruction.getValue(),
                    quantity,
                    status.getValue()
            );

            try {

                // SAVE RECORD TO MYSQL
                stockItemService.saveStockItem(newItem);

                // REFRESH TABLE
                refreshGrid();

                // CLOSE FORM
                dialog.close();

                // SUCCESS MESSAGE
                Notification notification = Notification.show(
                        "Stock saved successfully!",
                        3000,
                        Notification.Position.TOP_CENTER
                );

                notification.addThemeVariants(
                        NotificationVariant.LUMO_SUCCESS
                );

            } catch (Exception exception) {

                // DISPLAY ERROR WITHOUT CLOSING FORM
                Notification notification = Notification.show(
                        "Unable to save stock. Please try again.",
                        5000,
                        Notification.Position.MIDDLE
                );

                notification.addThemeVariants(
                        NotificationVariant.LUMO_ERROR
                );

            }
        });

        // FORM BUTTONS
        HorizontalLayout buttons = new HorizontalLayout(
                cancel,
                save
        );

        buttons.setJustifyContentMode(
                JustifyContentMode.END
        );

        buttons.setWidthFull();

        // ASSEMBLE DIALOG
        VerticalLayout dialogContent = new VerticalLayout(
                formTitle,
                form,
                buttons
        );

        dialogContent.setPadding(true);
        dialogContent.setSpacing(true);

        dialog.add(dialogContent);

        // DISPLAY FORM
        dialog.open();
    }

    // =========================================
// EDIT EXISTING STOCK
// =========================================

    private void openEditStockDialog(StockItem stockItem) {

        Dialog dialog = new Dialog();

        dialog.setWidth("550px");
        dialog.setMaxWidth("95vw");

        H3 formTitle = new H3("Edit Stock");

        formTitle.getStyle()
                .set("color", "#1687d9");

        // ORDER ID
        TextField orderId = new TextField("Order ID");
        orderId.setValue(stockItem.getOrderId());

        // PRODUCT
        TextField product = new TextField("Product");
        product.setValue(stockItem.getProduct());

        // CATEGORY
        TextField category = new TextField("Category");
        category.setValue(
                stockItem.getCategory() == null
                        ? ""
                        : stockItem.getCategory()
        );

        // SALES CHANNEL
        TextField salesChannel = new TextField("Sales Channel");
        salesChannel.setValue(
                stockItem.getSalesChannel() == null
                        ? ""
                        : stockItem.getSalesChannel()
        );

        // INSTRUCTION
        TextField instruction = new TextField("Instruction");
        instruction.setValue(
                stockItem.getInstruction() == null
                        ? ""
                        : stockItem.getInstruction()
        );

        // QUANTITY
        IntegerField items = new IntegerField("Items");
        items.setMin(0);
        items.setStepButtonsVisible(true);
        items.setValue(stockItem.getItems());

        // STATUS
        Select<String> status = new Select<>();
        status.setLabel("Status");
        status.setItems("Completed", "Pending");
        status.setValue(
                "Completed".equals(stockItem.getStatus())
                        ? "Completed"
                        : "Pending"
        );

        // FORM LAYOUT
        FormLayout form = new FormLayout();

        form.add(
                orderId,
                product,
                category,
                salesChannel,
                instruction,
                items,
                status
        );

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("450px", 2)
        );

        // CANCEL BUTTON
        Button cancel = new Button(
                "Cancel",
                event -> dialog.close()
        );

        // SAVE CHANGES BUTTON
        Button save = new Button("Save Changes");

        save.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        save.getStyle()
                .set("background", "#1687d9")
                .set("color", "white");

        save.addClickListener(event -> {

            // VALIDATE REQUIRED FIELDS
            if (orderId.getValue().trim().isEmpty()
                    || product.getValue().trim().isEmpty()) {

                Notification.show(
                        "Order ID and Product are required"
                );

                return;
            }

            Integer quantity = items.getValue();

            if (quantity == null || quantity < 0) {

                Notification.show(
                        "Please enter a valid quantity"
                );

                return;
            }

            // UPDATE THE EXISTING DATABASE ENTITY
            stockItem.setOrderId(orderId.getValue().trim());
            stockItem.setProduct(product.getValue().trim());
            stockItem.setCategory(category.getValue());
            stockItem.setSalesChannel(salesChannel.getValue());
            stockItem.setInstruction(instruction.getValue());
            stockItem.setItems(quantity);
            stockItem.setStatus(status.getValue());

            try {

                // SAVE CHANGES TO MYSQL
                stockItemService.saveStockItem(stockItem);

                // REFRESH TABLE
                refreshGrid();

                // CLOSE EDIT FORM
                dialog.close();

                Notification notification = Notification.show(
                        "Stock updated successfully!",
                        3000,
                        Notification.Position.TOP_CENTER
                );

                notification.addThemeVariants(
                        NotificationVariant.LUMO_SUCCESS
                );

            } catch (Exception exception) {

                Notification.show(
                        "Unable to update stock. Please try again."
                );
            }
        });

        HorizontalLayout buttons = new HorizontalLayout(
                cancel,
                save
        );

        buttons.setWidthFull();
        buttons.setJustifyContentMode(
                JustifyContentMode.END
        );

        VerticalLayout dialogContent = new VerticalLayout(
                formTitle,
                form,
                buttons
        );

        dialogContent.setPadding(true);

        dialog.add(dialogContent);

        dialog.open();
    }

    // =========================================
    // SIDEBAR BUTTON DESIGN
    // =========================================

    private VerticalLayout sidebarButton(
            VaadinIcon icon,
            String text
    ) {

        VerticalLayout container = new VerticalLayout();

        container.setPadding(false);
        container.setSpacing(false);
        container.setAlignItems(Alignment.CENTER);

        container.setWidthFull();
        container.setHeight("70px");

        container.getStyle()
                .set("color", "white")
                .set("cursor", "pointer")
                .set("font-size", "11px");

        Icon buttonIcon = new Icon(icon);

        Span label = new Span(text);

        container.add(
                buttonIcon,
                label
        );

        return container;
    }
}