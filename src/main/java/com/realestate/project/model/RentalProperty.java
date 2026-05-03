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
    private double advanceAmount;
    private String duration;

    public RentalProperty(String title, String location, double price, String description,double monthlyRent){
        super(title,location,price,description);
        this.monthlyRent = monthlyRent;

    }

    public RentalProperty(String title, String location, double price, String description, double monthlyRent, String duration){
        this(title, location, price, description, monthlyRent, 0, duration);
    }

    public RentalProperty(String title, String location, double price, String description, double monthlyRent, double advanceAmount, String duration){
        super(title,location,price,description);
        this.monthlyRent = monthlyRent;
        this.advanceAmount = advanceAmount;
        this.duration = duration;

    }

    @Override
    public String getPropertyCategory(){
        return "Rental Property";
    }


}
