package com.example.inventory.repository;

import com.example.inventory.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>{

    // Suche nach Namensteil, unabhängig von Groß-/Kleinschreibung
    List<Item> findByNameContainingIgnoreCase(String name);

    //alle Items finden, deren Bestand unter Limit sind
    List<Item> findByQuantityLessThan(int limit);
}
