package com.realestate.project.dto;

import com.realestate.project.model.Transaction.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AnalyticsReportDTO {

    @NotBlank(message = "Property type is required")
    @Pattern(regexp = "^(House|Apartments|Rental Property)$",
            message = "Property type must be House, Apartments, or Rental Property")
    private String propertyType;

    @NotBlank(message = "Property name is required")
    @Size(min = 2, max = 80, message = "Property name must be 2 to 80 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 .,'#&()/\\-]{1,79}$",
            message = "Property name contains invalid characters")
    private String propertyName;

    @NotBlank(message = "Client name is required")
    @Size(min = 2, max = 70, message = "Client name must be 2 to 70 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z .'-]{1,69}$",
            message = "Client name must contain only letters, spaces, apostrophes, hyphens, or periods")
    private String clientName;

    @NotNull(message = "Status is required")
    private TransactionStatus status;

    @NotBlank(message = "Value is required")
    @Size(max = 20, message = "Value is too long")
    @Pattern(regexp = "^[Rr][Ss]\\.?\\s?(\\d{1,3}(,\\d{3})+|\\d+)(\\.\\d{1,2})?\\s?([KkMm])?$",
            message = "Value must use Rs format, e.g. Rs 1.2M or Rs 980K")
    private String value;

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

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
