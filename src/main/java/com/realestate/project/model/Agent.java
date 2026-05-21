package com.realestate.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String sales;

    @Column(nullable = false)
    private String count;

    private String email;

    private String phone;

    private String address;

    @Column(name = "assigned_area")
    private String assignedArea;

    @Column(name = "commission_rate")
    private String commissionRate;

    private String status;

    private String specialization;

    private String experience;

    @Column(name = "profile_image")
    private String profileImage;

    public Agent(String name, String role, String sales, String count) {
        this.name = name;
        this.role = role;
        this.sales = sales;
        this.count = count;
    }
}
