package com.realestate.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "listings")
@Data
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerName;
    private String propertyTitle;
    private String propertyType;
    private Double price;
    private String location;

    @Column(columnDefinition = "LONGTEXT")
    private String imagePath;

    private String description;
}