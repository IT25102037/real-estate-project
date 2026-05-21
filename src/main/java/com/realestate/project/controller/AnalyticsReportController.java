package com.realestate.project.controller;

import com.realestate.project.dto.AnalyticsReportDTO;
import com.realestate.project.model.AnalyticsReport;
import com.realestate.project.service.AnalyticsReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        return analyticsReportService.deleteReport(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
