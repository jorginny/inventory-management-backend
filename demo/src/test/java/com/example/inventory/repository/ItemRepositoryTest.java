package com.example.inventory.repository;

import com.example.inventory.model.Item;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void saveAndFindById_returnsPersistedItem() {
        Item item = new Item("Laptop", "Mobiles Gerät", 5);

        Item saved = itemRepository.save(item);
        itemRepository.flush(); // sicherstellen, dass auf DB geschrieben wurde

        Optional<Item> found = itemRepository.findById(saved.getId());

        assertThat(saved.getId()).isNotNull();
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Laptop");
        assertThat(found.get().getQuantity()).isEqualTo(5);
    }

    // 2) findAll liefert alle Items
    @Test
    void findAll_returnsAllItems() {
        itemRepository.save(new Item("A", "Desc", 1));
        itemRepository.save(new Item("B", "Desc", 2));
        itemRepository.flush();

        List<Item> all = itemRepository.findAll();
        assertThat(all).extracting(Item::getName).contains("A", "B");
    }

    // 3) Löschen entfernt Item
    @Test
    void deleteById_removesItem() {
        Item saved = itemRepository.save(new Item("ToDelete", "Desc", 1));
        Long id = saved.getId();

        itemRepository.deleteById(id);
        itemRepository.flush();

        assertThat(itemRepository.findById(id)).isEmpty();
    }

    // 4) Bean Validation (z. B. @NotBlank, @Min)
    @Test
    void save_invalidItem_throwsConstraintViolation() {
        // @NotBlank private String name;
        // @Min(0) private int quantity;
        Item invalid = new Item("", "Desc", -1);

        assertThrows(ConstraintViolationException.class, () -> {
            itemRepository.save(invalid);
            itemRepository.flush();
        });
    }

    void findByNameContainingIgnoreCase_returnsMatches() {
        itemRepository.save(new Item("Laptop", "Mobiles Gerät", 5));
        itemRepository.save(new Item("LAserdrucker", "Bürogerät", 2));
        itemRepository.flush();

        List<Item> matches = itemRepository.findByNameContainingIgnoreCase("la");

        assertThat(matches).extracting(Item::getName)
                .containsExactlyInAnyOrder("Laptop", "LAserdrucker");
    }

    @Test
    void findByQuantityLessThan_returnsOnlyLowStockItems() {
        itemRepository.save(new Item("Laptop", "Mobiles Gerät", 5));
        itemRepository.save(new Item("Maus", "Zubehör", 1));
        itemRepository.flush();

        List<Item> matches = itemRepository.findByQuantityLessThan(3);

        assertThat(matches).extracting(Item::getName).containsExactly("Maus");
    }
}
