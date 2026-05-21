package com.realestate.project.controller;

import com.realestate.project.dto.FinanceTransactionRecordDTO;
import com.realestate.project.service.FinanceTransactionRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance-transactions")
@CrossOrigin(origins = "*")
public class FinanceTransactionRecordController {

    private final FinanceTransactionRecordService financeTransactionRecordService;

    public FinanceTransactionRecordController(FinanceTransactionRecordService financeTransactionRecordService) {
        this.financeTransactionRecordService = financeTransactionRecordService;
    }

    @GetMapping
    public ResponseEntity<List<FinanceTransactionRecordDTO>> getAll() {
        return ResponseEntity.ok(financeTransactionRecordService.getAllRecords());
    }
}
