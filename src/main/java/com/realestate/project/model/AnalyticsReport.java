package com.realestate.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_reports")
public class AnalyticsReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, name = "property_type")
    private String propertyType;

    @Column(nullable = true, name = "property_name")
    private String propertyName;

    @Column(nullable = true, name = "client_name")
    private String clientName;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Transaction.TransactionStatus status;

    @Column(nullable = true, name = "report_value")
    private String value;

    private String address;

    private String district;

    @Column(name = "agent_name")
    private String agentName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "report_type")
    private String reportType; // DAILY, WEEKLY, MONTHLY, TRANSACTION, AGENT

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Lob
    @Column(name = "report_data", columnDefinition = "LONGTEXT")
    private String reportData;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
