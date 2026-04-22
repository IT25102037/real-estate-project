package com.realestate.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "contact_messages")
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String userEmail;
    private String inquiryType;
    private String userPhone;
    // Inside ContactMessage.java
    private String status = "New";

    @Column(length = 1000)
    private String userMessage;
}
