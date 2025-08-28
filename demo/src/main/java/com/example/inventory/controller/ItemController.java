package com.example.inventory.controller;

import com.example.inventory.exception.ItemNotFoundException;
import com.example.inventory.model.Item;
import com.example.inventory.repository.ItemRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // -------- GET all --------
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemRepository.findAll()); // 200
    }

    // -------- GET by ID --------
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok) // 200 OK
                .orElseThrow(() -> new ItemNotFoundException(id)); // 404
    }

    // -------- POST --------
    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
        Item savedItem = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem); // 201 Created
    }


    // -------- PUT (Update) --------
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item newItem) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setName(newItem.getName());
                    existingItem.setDescription(newItem.getDescription());
                    existingItem.setQuantity(newItem.getQuantity());
                    itemRepository.save(existingItem);
                    return ResponseEntity.ok(existingItem); // 200 OK
                })
                .orElseThrow(() -> new ItemNotFoundException(id)); // 404
    }

    // -------- DELETE --------
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    itemRepository.delete(existingItem);
                    return ResponseEntity.<Void>noContent().build(); //
                })
                .orElseThrow(() -> new ItemNotFoundException(id)); // 404
    }
}
