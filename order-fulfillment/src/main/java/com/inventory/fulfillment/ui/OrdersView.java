package com.inventory.fulfillment.ui;
import com.inventory.fulfillment.domain.*;
import com.inventory.fulfillment.service.OrderService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.Route;
import java.util.*;
@Route("") public class OrdersView extends VerticalLayout {
private final OrderService service; private final Grid<CustomerOrder> grid=new Grid<>(CustomerOrder.class,false);private final TextField search=new TextField();private final Select<OrderStatus> status=new Select<>();
public OrdersView(OrderService service){this.service=service;setSizeFull();setPadding(true);setSpacing(true);getStyle().set("background","#f6f7fb");
H2 heading=new H2("Orders & Logistics");heading.getStyle().set("color","#6825b5");Button add=new Button("+ New Order",e->newOrder());add.getStyle().set("background","#7025c2").set("color","white");HorizontalLayout top=new HorizontalLayout(heading,add);top.setWidthFull();top.setJustifyContentMode(JustifyContentMode.BETWEEN);top.setAlignItems(Alignment.CENTER);add(top);
search.setPlaceholder("Search customer, product or order ID");search.setWidth("320px");search.addValueChangeListener(e->refresh());status.setPlaceholder("All statuses");status.setItems(OrderStatus.values());status.setEmptySelectionAllowed(true);status.addValueChangeListener(e->refresh());Button reload=new Button("Refresh",e->refresh());add(new HorizontalLayout(search,status,reload));
grid.addColumn(o->"#"+o.getId()).setHeader("Order ID").setAutoWidth(true);grid.addColumn(CustomerOrder::getCreatedAt).setHeader("Date").setAutoWidth(true);grid.addColumn(CustomerOrder::getCustomer).setHeader("Customer");grid.addColumn(CustomerOrder::getSalesChannel).setHeader("Sales channel");grid.addColumn(CustomerOrder::getDestination).setHeader("Destination");grid.addColumn(CustomerOrder::getProduct).setHeader("Product");grid.addColumn(CustomerOrder::getQuantity).setHeader("Items");grid.addColumn(CustomerOrder::getStatus).setHeader("Status");grid.addComponentColumn(o->new Button("Manage",e->manage(o))).setHeader("Actions");grid.setWidthFull();add(grid);refresh();}
private void refresh(){String q=search.getValue().trim().toLowerCase();OrderStatus s=status.getValue();grid.setItems(service.all().stream().filter(o->s==null||o.getStatus()==s).filter(o->q.isBlank()||String.valueOf(o.getId()).contains(q)||o.getCustomer().toLowerCase().contains(q)||o.getProduct().toLowerCase().contains(q)).toList());}
private void newOrder(){com.vaadin.flow.component.dialog.Dialog d=new com.vaadin.flow.component.dialog.Dialog();TextField customer=new TextField("Customer"),destination=new TextField("Destination"),product=new TextField("Product");IntegerField qty=new IntegerField("Quantity");qty.setMin(1);qty.setValue(1);Button save=new Button("Create order",e->{try{service.create(customer.getValue(),destination.getValue(),product.getValue(),qty.getValue()==null?0:qty.getValue());d.close();refresh();Notification.show("Order created");}catch(Exception ex){Notification.show(ex.getMessage());}});VerticalLayout form=new VerticalLayout(new H3("New order"),customer,destination,product,qty,save);d.add(form);d.open();}
private void manage(CustomerOrder o){com.vaadin.flow.component.dialog.Dialog d=new com.vaadin.flow.component.dialog.Dialog();Select<OrderStatus> next=new Select<>();next.setLabel("Status");next.setItems(OrderStatus.values());next.setValue(o.getStatus());TextField courier=new TextField("Courier");courier.setValue(o.getCourier()==null?"":o.getCourier());TextField tracking=new TextField("Tracking number");tracking.setValue(o.getTrackingNumber()==null?"":o.getTrackingNumber());Button save=new Button("Save",e->{try{service.update(o.getId(),next.getValue(),courier.getValue(),tracking.getValue());d.close();refresh();Notification.show("Updated");}catch(Exception ex){Notification.show(ex.getMessage());}});d.add(new VerticalLayout(new H3("Manage order #"+o.getId()),next,courier,tracking,save));d.open();}
}
