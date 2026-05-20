package com.realestate.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_accounts")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;

    private String accountHolderName;

    private String accountNumber;

    private String branchName;

    private Long userId;
}
