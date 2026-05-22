package com.realestate.project.service;

import com.realestate.project.model.ContactMessage;
import com.realestate.project.model.Property;
import com.realestate.project.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsDashboardService {

    private final CustomerService customerService;
    private final PropertyService propertyService;
    private final ContactService contactService;
    private final TransactionService transactionService;

    public AnalyticsDashboardService(CustomerService customerService,
                                     PropertyService propertyService,
                                     ContactService contactService,
                                     TransactionService transactionService) {
        this.customerService = customerService;
        this.propertyService = propertyService;
        this.contactService = contactService;
        this.transactionService = transactionService;
    }

    public Map<String, Object> buildReportSnapshot(LocalDate start, LocalDate end) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("periodStart", start.toString());
        snapshot.put("periodEnd", end.toString());
        snapshot.put("totalCustomers", customerService.getAllCustomers().size());
        snapshot.put("activeListings", propertyService.getAllProperties().size());
        snapshot.put("inboxMessages", contactService.getAllMessages().size());

        List<Transaction> transactions = transactionService.getAllTransactions();
        snapshot.put("totalTransactions", transactions.size());

        double revenue = 0.0;
        long sold = 0;
        long pending = 0;
        long rented = 0;
        for (Transaction tx : transactions) {
            revenue += parseValue(tx.getValue());
            if (tx.getStatus() == Transaction.TransactionStatus.Sold) {
                sold++;
            } else if (tx.getStatus() == Transaction.TransactionStatus.Pending) {
                pending++;
            } else if (tx.getStatus() == Transaction.TransactionStatus.Rented) {
                rented++;
            }
        }
        snapshot.put("totalRevenue", revenue);
        snapshot.put("soldCount", sold);
        snapshot.put("pendingCount", pending);
        snapshot.put("rentedCount", rented);

        Map<String, Long> messageStatus = new HashMap<>();
        for (ContactMessage message : contactService.getAllMessages()) {
            String status = message.getStatus() != null ? message.getStatus() : "New";
            messageStatus.merge(status, 1L, Long::sum);
        }
        snapshot.put("messageStatusCounts", messageStatus);

        Map<String, Long> districts = new HashMap<>();
        for (Property property : propertyService.getAllProperties()) {
            String district = property.getDistrict() != null ? property.getDistrict() : "Other";
            districts.merge(district, 1L, Long::sum);
        }
        snapshot.put("listingDistricts", districts);
        return snapshot;
    }

    private double parseValue(String value) {
        if (value == null || value.isBlank()) {
            return 0.0;
        }
        String clean = value.replaceAll("[^0-9.KkMm]", "");
        if (clean.isEmpty()) {
            return 0.0;
        }
        boolean hasM = clean.toUpperCase().endsWith("M");
        boolean hasK = clean.toUpperCase().endsWith("K");
        String numeric = clean.replaceAll("[KkMm]", "");
        try {
            double amount = Double.parseDouble(numeric.replace(",", ""));
            if (hasM) {
                return amount * 1_000_000.0;
            }
            if (hasK) {
                return amount * 1_000.0;
            }
            return amount;
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }
}
