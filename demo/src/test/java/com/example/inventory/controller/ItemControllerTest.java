package com.example.inventory.controller;

import com.example.inventory.model.Item;
import com.example.inventory.repository.ItemRepository;
import com.example.inventory.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@Import(GlobalExceptionHandler.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // --------- Alter Test: GET /api/items/{id} nicht vorhanden
    @Test
    void getItem_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/items/9999")) // ID die es nicht gibt
                .andExpect(status().isNotFound());
    }

    // --------- Neuer Test: POST /api/items gültiges Item
    @Test
    void testCreateItem_validItem_returnsCreated() throws Exception {
        Item item = new Item("TestItem", "Beschreibung", 5);

        when(itemRepository.save(item)).thenReturn(new Item("TestItem", "Beschreibung", 5));

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated());
    }

    // --------- Neuer Test: POST /api/items ungültiges Item
    @Test
    void testCreateItem_invalidItem_returnsBadRequest() throws Exception {
        Item item = new Item(null, "", -1);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteItem_existingItem_returnsNoContent() throws Exception {
        Item existingItem = new Item("TestItem", "Beschreibung", 5);
        existingItem.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));

        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isNoContent()); // Erwartet 204
    }

    @Test
    void testDeleteItem_notFound_returns404() throws Exception {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/items/99"))
                .andExpect(status().isNotFound()); // Erwartet 404
    }

    @Test
    void testUpdateItem_existingItem_returnsUpdated() throws Exception {
        Item existingItem = new Item("Alt", "Alte Beschreibung", 1);
        existingItem.setId(1L);

        Item updatedItem = new Item("Neu", "Neue Beschreibung", 10);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Neu"))
                .andExpect(jsonPath("$.description").value("Neue Beschreibung"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void testUpdateItem_notFound_returns404() throws Exception {
        Item updatedItem = new Item("Neu", "Neue Beschreibung", 10);

        when(itemRepository.findById(42L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/items/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isNotFound());
    }
}
