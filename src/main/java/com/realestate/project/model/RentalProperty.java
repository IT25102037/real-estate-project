package com.realestate.project.model;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor

public class RentalProperty extends Property {

    private double monthlyRent;

    public RentalProperty(String title, String location, double price, String description,double monthlyRent){
        super(title,location,price,description);
        this.monthlyRent = monthlyRent;

    }

    @Override
    public String getPropertyCategory(){
        return "Rental Property";
    }
}
