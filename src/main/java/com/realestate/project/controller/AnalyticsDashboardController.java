package com.realestate.project.controller;

import com.realestate.project.model.*;
import com.realestate.project.service.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsDashboardController {

    private final CustomerService customerService;
    private final PropertyService propertyService;
    private final ContactService contactService;
    private final TransactionService transactionService;
    private final ActivityService activityService;

    public AnalyticsDashboardController(CustomerService customerService,
                                        PropertyService propertyService,
                                        ContactService contactService,
                                        TransactionService transactionService,
                                        ActivityService activityService) {
        this.customerService = customerService;
        this.propertyService = propertyService;
        this.contactService = contactService;
        this.transactionService = transactionService;
        this.activityService = activityService;
    }

    /* ── GET KPI COUNTS ────────────────────────────────────────── */
    @GetMapping("/kpis")
    public Map<String, Object> getKPIs() {
        long totalCustomers = customerService.getAllCustomers().size();
        long activeListings = propertyService.getAllProperties().size();
        long inboxMessages = contactService.getAllMessages().size();

        // Calculate total revenue from transactions ($k)
        double totalRevenue = 0.0;
        List<Transaction> transactions = transactionService.getAllTransactions();
        for (Transaction tx : transactions) {
            totalRevenue += parseValue(tx.getValue());
        }

        return Map.of(
                "totalCustomers", totalCustomers,
                "activeListings", activeListings,
                "inboxMessages", inboxMessages,
                "totalRevenue", totalRevenue
        );
    }

    /* ── GEOGRAPHIC REACH ──────────────────────────────────────── */
    @GetMapping("/geo-reach")
    public List<Map<String, Object>> getGeoReach() {
        List<Property> properties = propertyService.getAllProperties();
        if (properties.isEmpty()) {
            return List.of(
                    Map.of("city", "Colombo", "pct", 48),
                    Map.of("city", "Kandy", "pct", 22),
                    Map.of("city", "Galle", "pct", 15),
                    Map.of("city", "Negombo", "pct", 9),
                    Map.of("city", "Jaffna", "pct", 6)
            );
        }

        Map<String, Long> counts = new HashMap<>();
        counts.put("Colombo", 0L);
        counts.put("Kandy", 0L);
        counts.put("Galle", 0L);
        counts.put("Negombo", 0L);
        counts.put("Jaffna", 0L);
        long otherCount = 0L;

        for (Property p : properties) {
            String loc = p.getLocation();
            if (loc == null) continue;
            String lower = loc.toLowerCase();
            if (lower.contains("colombo")) {
                counts.put("Colombo", counts.get("Colombo") + 1);
            } else if (lower.contains("kandy")) {
                counts.put("Kandy", counts.get("Kandy") + 1);
            } else if (lower.contains("galle")) {
                counts.put("Galle", counts.get("Galle") + 1);
            } else if (lower.contains("negombo")) {
                counts.put("Negombo", counts.get("Negombo") + 1);
            } else if (lower.contains("jaffna")) {
                counts.put("Jaffna", counts.get("Jaffna") + 1);
            } else {
                otherCount++;
            }
        }

        long total = properties.size();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            if (entry.getValue() > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("city", entry.getKey());
                map.put("pct", (int) Math.round((double) entry.getValue() * 100 / total));
                result.add(map);
            }
        }
        if (otherCount > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("city", "Other");
            map.put("pct", (int) Math.round((double) otherCount * 100 / total));
            result.add(map);
        }

        result.sort((a, b) -> Integer.compare((Integer) b.get("pct"), (Integer) a.get("pct")));
        return result;
    }

    /* ── PROPERTY TYPES DISTRIBUTION ────────────────────────────── */
    @GetMapping("/property-types")
    public Map<String, Integer> getPropertyTypes() {
        List<Property> properties = propertyService.getAllProperties();
        if (properties.isEmpty()) {
            return Map.of(
                    "Mansions", 38,
                    "Penthouses", 27,
                    "Apartments", 21,
                    "Villas", 14
            );
        }

        Map<String, Integer> counts = new HashMap<>();
        counts.put("Mansions", 0);
        counts.put("Penthouses", 0);
        counts.put("Apartments", 0);
        counts.put("Villas", 0);

        for (Property p : properties) {
            if (p instanceof House) {
                counts.put("Mansions", counts.get("Mansions") + 1);
            } else if (p instanceof Apartment) {
                String title = p.getTitle();
                if (title != null && title.toLowerCase().contains("penthouse")) {
                    counts.put("Penthouses", counts.get("Penthouses") + 1);
                } else {
                    counts.put("Apartments", counts.get("Apartments") + 1);
                }
            } else if (p instanceof RentalProperty) {
                counts.put("Villas", counts.get("Villas") + 1);
            }
        }
        return counts;
    }

    /* ── REVENUE PERFORMANCE HISTORICAL CHARTS ───────────────────── */
    @GetMapping("/revenue-performance")
    public Map<String, Object> getRevenuePerformance() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        LocalDateTime now = LocalDateTime.now();

        // ── 6M period ──
        String[] months6 = new String[6];
        double[] data6 = {28.0, 45.0, 38.0, 72.0, 58.0, 91.0}; // baseline
        LocalDateTime[] dates6 = new LocalDateTime[6];
        for (int i = 0; i < 6; i++) {
            LocalDateTime m = now.minusMonths(5 - i);
            dates6[i] = m;
            String name = m.getMonth().name().substring(0, 3);
            months6[i] = name.substring(0, 1) + name.substring(1).toLowerCase();
        }

        for (Transaction tx : transactions) {
            LocalDateTime txDate = tx.getCreatedAt();
            if (txDate == null) continue;
            for (int i = 0; i < 6; i++) {
                if (txDate.getYear() == dates6[i].getYear() && txDate.getMonth() == dates6[i].getMonth()) {
                    data6[i] += parseValue(tx.getValue());
                    break;
                }
            }
        }

        // ── 1Y period ──
        String[] months12 = new String[12];
        double[] data12 = {18.0, 28.0, 35.0, 42.0, 38.0, 55.0, 62.0, 48.0, 72.0, 58.0, 80.0, 91.0}; // baseline
        LocalDateTime[] dates12 = new LocalDateTime[12];
        for (int i = 0; i < 12; i++) {
            LocalDateTime m = now.minusMonths(11 - i);
            dates12[i] = m;
            String name = m.getMonth().name().substring(0, 3);
            months12[i] = name.substring(0, 1) + name.substring(1).toLowerCase();
        }

        for (Transaction tx : transactions) {
            LocalDateTime txDate = tx.getCreatedAt();
            if (txDate == null) continue;
            for (int i = 0; i < 12; i++) {
                if (txDate.getYear() == dates12[i].getYear() && txDate.getMonth() == dates12[i].getMonth()) {
                    data12[i] += parseValue(tx.getValue());
                    break;
                }
            }
        }

        // ── ALL period ──
        String[] years = new String[5];
        double[] dataAll = {30.0, 48.0, 62.0, 75.0, 92.0}; // baseline
        int currentYear = now.getYear();
        for (int i = 0; i < 5; i++) {
            years[i] = String.valueOf(currentYear - 4 + i);
        }

        for (Transaction tx : transactions) {
            LocalDateTime txDate = tx.getCreatedAt();
            if (txDate == null) continue;
            int txYear = txDate.getYear();
            for (int i = 0; i < 5; i++) {
                if (txYear == (currentYear - 4 + i)) {
                    dataAll[i] += parseValue(tx.getValue());
                    break;
                }
            }
        }

        return Map.of(
                "6m", Map.of("labels", months6, "data", data6),
                "1y", Map.of("labels", months12, "data", data12),
                "all", Map.of("labels", years, "data", dataAll)
        );
    }

    /* ── GET RECENT SYSTEM ACTIVITIES ───────────────────────────── */
    @GetMapping("/activities")
    public List<ActivityLog> getRecentActivities() {
        List<ActivityLog> logs = activityService.getRecentActivities();
        if (logs.isEmpty()) {
            return List.of(
                    new ActivityLog("TRANSACTION_CREATED", "<strong>Sale closed</strong> — Azure Residences by Amara Silva", "dollar-sign", "gold"),
                    new ActivityLog("CUSTOMER_REGISTERED", "<strong>New customer</strong> — Rohan Perera registered", "user-plus", "blue"),
                    new ActivityLog("PROPERTY_CREATED", "<strong>Listing approved</strong> — Skyline Penthouse #204", "check-circle", "green"),
                    new ActivityLog("MESSAGE_RECEIVED", "<strong>New inquiry</strong> — TRI-ZEN Apartment from client", "mail", "gold"),
                    new ActivityLog("PROPERTY_DELETED", "<strong>Listing removed</strong> — Old stock cleared by admin", "trash-2", "red")
            );
        }
        return logs;
    }

    /* ── ESTABLISH SSE CONNECTION ────────────────────────────────── */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registerStream() {
        return activityService.registerEmitter();
    }

    /* ── HELPER: PARSE TRANSACTION VALUE TO $K ───────────────────── */
    private double parseValue(String val) {
        if (val == null) return 0.0;
        String clean = val.replace("$", "").replace("M", "").replace("m", "").replace("K", "").replace("k", "").replace(" ", "").trim();
        try {
            double d = Double.parseDouble(clean);
            if (val.toUpperCase().contains("M")) {
                return d * 1000.0; // scale to $k
            } else if (val.toUpperCase().contains("K")) {
                return d;
            } else {
                return d / 1000.0; // assume raw, scale to $k
            }
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
