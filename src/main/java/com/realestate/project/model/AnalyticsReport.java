package com.realestate.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_reports")
public class AnalyticsReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "property_type")
    private String propertyType;

    @Column(nullable = false, name = "property_name")
    private String propertyName;

    @Column(nullable = false, name = "client_name")
    private String clientName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Transaction.TransactionStatus status;

    @Column(nullable = false, name = "report_value")
    private String value;

    private String address;

    private String district;

    @Column(name = "agent_name")
    private String agentName;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public AnalyticsReport() {
    }

    public AnalyticsReport(String propertyType,
                           String propertyName,
                           String clientName,
                           Transaction.TransactionStatus status,
                           String value,
                           String address,
                           String district,
                           String agentName) {
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.clientName = clientName;
        this.status = status;
        this.value = value;
        this.address = address;
        this.district = district;
        this.agentName = agentName;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

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

    public Transaction.TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(Transaction.TransactionStatus status) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
