package com.example.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Item {

    @GeneratedValue
    @Id
    private long id;

    @NotBlank(message = "Name darf nicht leer sein")
    private String name;

    private String description;

    @Min(value = 0, message = "Menge darf nicht negativ sein")
    private int quantity;

    public Item(){

    }

    public Item(String name, String description, int quantity){
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    //Getter und Setter


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
