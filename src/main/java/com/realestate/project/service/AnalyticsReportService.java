package com.realestate.project.service;

import com.realestate.project.dto.AnalyticsReportDTO;
import com.realestate.project.model.AnalyticsReport;
import com.realestate.project.model.Transaction;
import com.realestate.project.repository.AnalyticsReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AnalyticsReportService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;
    private final AnalyticsReportRepository analyticsReportRepository;
    private final ActivityService activityService;
    private final AnalyticsDashboardService analyticsDashboardService;

    public AnalyticsReportService(AnalyticsReportRepository analyticsReportRepository,
                                  ActivityService activityService,
                                  AnalyticsDashboardService analyticsDashboardService) {
        this.analyticsReportRepository = analyticsReportRepository;
        this.activityService = activityService;
        this.analyticsDashboardService = analyticsDashboardService;
    }

    public List<AnalyticsReport> getAllReports() {
        return analyticsReportRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<AnalyticsReport> getReportById(Long id) {
        return analyticsReportRepository.findById(id);
    }

    public AnalyticsReport createReport(AnalyticsReportDTO dto) {
        AnalyticsReport report = mapDtoToEntity(new AnalyticsReport(), dto);
        AnalyticsReport saved = analyticsReportRepository.save(report);
        logActivity("REPORT_CREATED", saved);
        return saved;
    }

    public Optional<AnalyticsReport> updateReport(Long id, AnalyticsReportDTO dto) {
        return analyticsReportRepository.findById(id).map(existing -> {
            AnalyticsReport saved = analyticsReportRepository.save(mapDtoToEntity(existing, dto));
            activityService.logActivity(
                    "REPORT_UPDATED",
                    "<strong>Report updated</strong> - " + saved.getTitle(),
                    "pencil",
                    "blue"
            );
            return saved;
        });
    }

    public boolean deleteReport(Long id) {
        return analyticsReportRepository.findById(id).map(report -> {
            analyticsReportRepository.delete(report);
            activityService.logActivity(
                    "REPORT_DELETED",
                    "<strong>Report deleted</strong> - " + report.getTitle(),
                    "trash-2",
                    "red"
            );
            return true;
        }).orElse(false);
    }

    public AnalyticsReport generatePeriodicReport(String type) {
        LocalDate end = LocalDate.now();
        LocalDate start = switch (type.toUpperCase()) {
            case "DAILY" -> end;
            case "WEEKLY" -> end.minusDays(6);
            case "MONTHLY" -> end.minusDays(29);
            default -> throw new IllegalArgumentException("Unsupported report type: " + type);
        };

        Map<String, Object> snapshot = analyticsDashboardService.buildReportSnapshot(start, end);
        AnalyticsReportDTO dto = new AnalyticsReportDTO();
        dto.setTitle(capitalize(type) + " Analytics Report · " + end.format(DATE_FMT));
        dto.setReportType(type.toUpperCase());
        dto.setStartDate(start.format(DATE_FMT));
        dto.setEndDate(end.format(DATE_FMT));
        dto.setReportData(writeJson(snapshot));
        dto.setClientName("Platform Overview");
        dto.setPropertyName("LuxeEstate Analytics");
        dto.setStatus(Transaction.TransactionStatus.Sold);

        return createReport(dto);
    }

    private AnalyticsReport mapDtoToEntity(AnalyticsReport report, AnalyticsReportDTO dto) {
        report.setTitle(dto.getTitle().trim());
        report.setReportType(dto.getReportType().trim().toUpperCase());
        report.setStartDate(trimOrNull(dto.getStartDate()));
        report.setEndDate(trimOrNull(dto.getEndDate()));
        report.setReportData(dto.getReportData());
        report.setPropertyType(trimOrNull(dto.getPropertyType()));
        report.setPropertyName(trimOrNull(dto.getPropertyName()));
        report.setClientName(trimOrNull(dto.getClientName()));
        report.setStatus(dto.getStatus());
        report.setValue(trimOrNull(dto.getValue()));
        report.setAddress(trimOrNull(dto.getAddress()));
        report.setDistrict(trimOrNull(dto.getDistrict()));
        report.setAgentName(trimOrNull(dto.getAgentName()));
        return report;
    }

    private String trimOrNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String writeJson(Map<String, Object> data) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                json.append(',');
            }
            first = false;
            json.append('"').append(escapeJson(entry.getKey())).append("\":");
            json.append(toJsonValue(entry.getValue()));
        }
        json.append('}');
        return json.toString();
    }

    private String toJsonValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> cast = new HashMap<>();
            map.forEach((key, val) -> cast.put(String.valueOf(key), val));
            return writeJson(cast);
        }
        return "\"" + escapeJson(String.valueOf(value)) + "\"";
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    private void logActivity(String type, AnalyticsReport saved) {
        activityService.logActivity(
                type,
                "<strong>Report saved</strong> - " + saved.getTitle(),
                "file-text",
                "green"
        );
    }
}
