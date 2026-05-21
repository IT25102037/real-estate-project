package com.realestate.project.controller;

import com.realestate.project.dto.AnalyticsReportDTO;
import com.realestate.project.model.AnalyticsReport;
import com.realestate.project.service.AnalyticsReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class AnalyticsReportController {

    private final AnalyticsReportService analyticsReportService;

    public AnalyticsReportController(AnalyticsReportService analyticsReportService) {
        this.analyticsReportService = analyticsReportService;
    }

    @GetMapping
    public ResponseEntity<List<AnalyticsReport>> getAllReports() {
        return ResponseEntity.ok(analyticsReportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalyticsReport> getReportById(@PathVariable Long id) {
        return analyticsReportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AnalyticsReport> createReport(@Valid @RequestBody AnalyticsReportDTO dto) {
        AnalyticsReport created = analyticsReportService.createReport(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/generate/{type}")
    public ResponseEntity<?> generateReport(@PathVariable String type) {
        try {
            AnalyticsReport created = analyticsReportService.generatePeriodicReport(type);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalyticsReport> updateReport(@PathVariable Long id,
                                                        @Valid @RequestBody AnalyticsReportDTO dto) {
        return analyticsReportService.updateReport(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        return analyticsReportService.deleteReport(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
