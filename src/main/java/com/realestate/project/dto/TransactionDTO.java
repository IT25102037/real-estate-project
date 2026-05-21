package com.realestate.project.dto;

import com.realestate.project.model.Transaction.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
    @Size(min = 2, max = 80, message = "Property name must be 2 to 80 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 .,'#&()/\\-]{1,79}$",
            message = "Property name contains invalid characters")
    private String property;

    @NotBlank(message = "Client name is required")
    @Size(min = 2, max = 70, message = "Client name must be 2 to 70 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z .'-]{1,69}$",
            message = "Client name must contain only letters, spaces, apostrophes, hyphens, or periods")
    private String client;

    @NotBlank(message = "Transaction value is required")
    @Size(max = 20, message = "Transaction value is too long")
    @Pattern(regexp = "^[Rr][Ss]\\.?\\s?(\\d{1,3}(,\\d{3})+|\\d+)(\\.\\d{1,2})?\\s?([KkMm])?$",
            message = "Transaction value must use Rs format, e.g. Rs 1.2M or Rs 980K")
    private String value;          // e.g. "Rs 2.4M"

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
