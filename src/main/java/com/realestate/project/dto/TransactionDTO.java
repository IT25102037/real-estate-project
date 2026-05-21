package com.realestate.project.dto;

import com.realestate.project.model.Transaction.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for Transaction create / update requests.
 * Matches the JSON body sent from analitics.html:
 *   { propertyType, property, client, value, status, address, district, agentName }
 *
 * File path:
 *   src/main/java/com/realestate/project/dto/TransactionDTO.java
 */
public class TransactionDTO {

    @NotBlank(message = "Property name is required")
    @Size(min = 2, max = 80, message = "Property name must be 2 to 80 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 .,'#&()/\\-]{1,79}$",
            message = "Property name contains invalid characters")
    private String property;

    @NotBlank(message = "Property type is required")
    @Pattern(regexp = "^(House|Apartments|Rental Property)$",
            message = "Property type must be House, Apartments, or Rental Property")
    private String propertyType;

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

    @NotBlank(message = "Address is required")
    @Size(min = 2, max = 160, message = "Address must be 2 to 160 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 .,'#&()/\\-]{1,159}$",
            message = "Address contains invalid characters")
    private String address;

    @NotBlank(message = "District is required")
    @Pattern(regexp = "^(Ampara|Anuradhapura|Badulla|Batticaloa|Colombo|Galle|Gampaha|Hambantota|Jaffna|Kalutara|Kandy|Kegalle|Kilinochchi|Kurunegala|Mannar|Matale|Matara|Monaragala|Mullaitivu|Nuwara Eliya|Polonnaruwa|Puttalam|Ratnapura|Trincomalee|Vavuniya)$",
            message = "District is invalid")
    private String district;

    @Size(max = 70, message = "Agent name must be at most 70 characters")
    @Pattern(regexp = "^$|^[A-Za-z][A-Za-z .'-]{1,69}$",
            message = "Agent name must contain only letters, spaces, apostrophes, hyphens, or periods")
    private String agentName;

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

    public String getPropertyType()            { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public String getClient()                  { return client; }
    public void setClient(String client)       { this.client = client; }

    public String getValue()                   { return value; }
    public void setValue(String value)         { this.value = value; }

    public TransactionStatus getStatus()                    { return status; }
    public void setStatus(TransactionStatus status)         { this.status = status; }

    public String getAddress()                 { return address; }
    public void setAddress(String address)     { this.address = address; }

    public String getDistrict()                { return district; }
    public void setDistrict(String district)   { this.district = district; }

    public String getAgentName()               { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
}
