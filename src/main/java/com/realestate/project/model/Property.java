package com.realestate.project.model;
import jakarta.persistence.*;

public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String loaction;
    private double price;
    private String type;
    private String description;

    public Property(){

        public Property()
    }


}
