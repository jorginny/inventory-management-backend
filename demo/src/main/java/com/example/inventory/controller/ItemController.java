package com.example.inventory.controller;

import com.example.inventory.model.Item;
import com.example.inventory.repository.ItemRepository;
import com.example.inventory.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    //get Alle Items abrufen
    @GetMapping
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    //post Neues Item erstellen
    @PostMapping
    public Item createItem(@RequestBody Item item){
        return itemRepository.save(item);
    }

    //Get Einzelnes Item abrufen
    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id){
        return itemRepository.findById(id).orElse(null);
    }

    // put Item aktualisieren
    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item itemDetails){
        Item item = itemRepository.findById(id).orElseThrow();
        item.setName(itemDetails.getName());
        item.setQuantity(itemDetails.getQuantity());
        return itemRepository.save(item);
    }

    // delete Item löschen
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id){
        itemRepository.deleteById(id);
    }

}
