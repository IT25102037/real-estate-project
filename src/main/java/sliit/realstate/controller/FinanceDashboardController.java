package sliit.realstate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sliit.realstate.dto.FinanceSummaryResponse;
import sliit.realstate.service.FinanceDashboardService;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class FinanceDashboardController {

    private final FinanceDashboardService
            financeDashboardService;

    @GetMapping("/summary")
    public FinanceSummaryResponse
    getSummary() {

        return financeDashboardService
                .getSummary();
    }
}