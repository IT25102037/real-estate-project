package com.realestate.project.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class House extends Property {

    private int numOfFloors;

    public House(String title, String address, String district, double price, String description, int numOfFloors) {
        super(title, address, district, price, description);
        this.numOfFloors = numOfFloors;
    }

    @Override
    public String getPropertyCategory() {
        return "House";
    }
}
