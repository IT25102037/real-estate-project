package com.realestate.project.dto;

import com.realestate.project.model.Transaction.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for Transaction create / update requests.
 * Matches the JSON body sent from analitics.html:
 *   { property, client, value, status }
 *
 * File location:
 *   src/main/java/com/realestate/project/dto/TransactionDTO.java
 */
public class TransactionDTO {

    @NotBlank(message = "Property name is required")
    private String property;

    @NotBlank(message = "Client name is required")
    private String client;

    @NotBlank(message = "Transaction value is required")
    private String value;          // e.g. "$2.4M"

    @NotNull(message = "Status is required")
    private TransactionStatus status;  // Sold | Pending | Rented

    /* ── Constructors ──────────────────────────────────────── */

    public TransactionDTO() {}

    public TransactionDTO(String property, String client, String value, TransactionStatus status) {
        this.property = property;
        this.client   = client;
        this.value    = value;
        this.status   = status;
    }

    /* ── Getters & Setters ─────────────────────────────────── */

    public String getProperty()                { return property; }
    public void setProperty(String property)   { this.property = property; }

    public String getClient()                  { return client; }
    public void setClient(String client)       { this.client = client; }

    public String getValue()                   { return value; }
    public void setValue(String value)         { this.value = value; }

    public TransactionStatus getStatus()                    { return status; }
    public void setStatus(TransactionStatus status)         { this.status = status; }
}
