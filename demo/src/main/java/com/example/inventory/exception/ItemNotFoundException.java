package com.example.inventory.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super("Item mit ID " + id + " wurde nicht gefunden.");
    }
}
