package com.inventory.pos.shell;

/**
 * The web addresses of the five original screens. The shell uses them for its header buttons. A new screen picks its
 * own address in its own {@code @Route} and does not need to be listed here.
 */
public final class Routes {

    public static final String REGISTER = "";
    public static final String INVOICES = "invoices";
    public static final String DEBTS = "debts";
    public static final String CUSTOMERS = "customers";
    public static final String INVENTORY = "inventory";

    private Routes() {
    }
}
