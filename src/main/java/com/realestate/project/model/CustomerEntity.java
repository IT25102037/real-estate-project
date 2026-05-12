package com.realestate.project.model;  //Model package usually contains entity / database classes

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;  //Lombok automatically create code for you

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id  //Makes the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //Auto increment the ID
    private Long id;

    private String name;
    private String email;
    private String phone;
    @Column(name = "property_type")
    private String propertyType;

    private String address;
    private String password;

}

//Hibernate used ti automatically creates tables

