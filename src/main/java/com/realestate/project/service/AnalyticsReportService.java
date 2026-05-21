package com.realestate.project.service;

import com.realestate.project.dto.AnalyticsReportDTO;
import com.realestate.project.model.AnalyticsReport;
import com.realestate.project.repository.AnalyticsReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AnalyticsReportService {
    private static final Pattern MONEY_PATTERN = Pattern.compile(
            "^[Rr][Ss]\\.?\\s?(\\d{1,3}(?:,\\d{3})+|\\d+)(\\.\\d{1,2})?\\s?([KkMm])?$"
    );

    private final AnalyticsReportRepository analyticsReportRepository;
    private final ActivityService activityService;

    public AnalyticsReportService(AnalyticsReportRepository analyticsReportRepository,
                                  ActivityService activityService) {
        this.analyticsReportRepository = analyticsReportRepository;
        this.activityService = activityService;
    }

    public List<AnalyticsReport> getAllReports() {
        return analyticsReportRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<AnalyticsReport> getReportById(Long id) {
        return analyticsReportRepository.findById(id);
    }

    public AnalyticsReport createReport(AnalyticsReportDTO dto) {
        AnalyticsReport report = new AnalyticsReport(
                dto.getPropertyType().trim(),
                dto.getPropertyName().trim(),
                dto.getClientName().trim(),
                dto.getStatus(),
                normalizeMoney(dto.getValue()),
                dto.getAddress().trim(),
                dto.getDistrict().trim(),
                normalizeOptionalName(dto.getAgentName())
        );
        AnalyticsReport saved = analyticsReportRepository.save(report);
        activityService.logActivity(
                "REPORT_CREATED",
                "<strong>Report created</strong> - " + saved.getPropertyName() + " for " + saved.getClientName(),
                "file-text",
                "green"
        );
        return saved;
    }

    public boolean deleteReport(Long id) {
        return analyticsReportRepository.findById(id).map(report -> {
            analyticsReportRepository.delete(report);
            activityService.logActivity(
                    "REPORT_DELETED",
                    "<strong>Report deleted</strong> - " + report.getPropertyName(),
                    "trash-2",
                    "red"
            );
            return true;
        }).orElse(false);
    }

    private String normalizeMoney(String value) {
        Matcher matcher = MONEY_PATTERN.matcher(value.trim());
        if (!matcher.matches()) {
            return value.trim();
        }

        String amount = matcher.group(1).replace(",", "");
        String decimal = matcher.group(2) == null ? "" : matcher.group(2);
        String suffix = matcher.group(3) == null ? "" : matcher.group(3).toUpperCase();
        return "Rs " + amount + decimal + suffix;
    }

    private String normalizeOptionalName(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
