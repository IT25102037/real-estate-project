package com.realestate.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a real estate transaction record.
 * Maps to the "transactions" table in the database.
 *
 * File location:
 *   src/main/java/com/realestate/project/model/Transaction.java
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String property;   // e.g. "Azure Residences"

    @Column(nullable = false)
    private String client;     // e.g. "Amara Silva"

    @Column(nullable = false)
    private String value;      // e.g. "Rs 2.4M"  (stored as string to match frontend format)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;  // SOLD | PENDING | RENTED

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /* ── Constructors ──────────────────────────────────────── */

    public Transaction() {}

    public Transaction(String property, String client, String value, TransactionStatus status) {
        this.property = property;
        this.client   = client;
        this.value    = value;
        this.status   = status;
        this.createdAt = LocalDateTime.now();
    }

    /* ── Getters & Setters ─────────────────────────────────── */

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public String getProperty()                { return property; }
    public void setProperty(String property)   { this.property = property; }

    public String getClient()                  { return client; }
    public void setClient(String client)       { this.client = client; }

    public String getValue()                   { return value; }
    public void setValue(String value)         { this.value = value; }

    public TransactionStatus getStatus()                    { return status; }
    public void setStatus(TransactionStatus status)         { this.status = status; }

    public LocalDateTime getCreatedAt()                     { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)       { this.createdAt = createdAt; }

    /* ── Nested Enum ───────────────────────────────────────── */

    public enum TransactionStatus {
        Sold, Pending, Rented
    }
}
