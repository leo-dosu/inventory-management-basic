package com.inventory.pos.customers.service;

public record CustomerForm(String name, String phone, String email, String address, String notes, Integer termsDays) {
}
