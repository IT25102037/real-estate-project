package com.realestate.project.model;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Apartment extends Property {

    private int floorNumber;

    public Apartment(String title, String address, String district, double price, String description, int floorNumber){
        super(title, address, district, price, description);
        this.floorNumber = floorNumber;

    }

    @Override
    public String getPropertyCategory(){
        return "Apartment";
    }
}



