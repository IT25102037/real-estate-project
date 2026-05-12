package com.realestate.project.controller;

import com.realestate.project.dto.TransactionDTO;
import com.realestate.project.model.Transaction;
import com.realestate.project.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for the Analytics page (analitics.html).
 *
 * Exposes:
 *   GET    /api/transactions          → all transactions (table)
 *   GET    /api/transactions/{id}     → single transaction
 *   POST   /api/transactions          → create  (from "Add Record" modal)
 *   PUT    /api/transactions/{id}     → update  (from "Edit Record" modal)
 *   DELETE /api/transactions/{id}     → delete  (from trash icon)
 *
 * NOTE:
 *   GET /api/customers  →  already served by your existing CustomerController
 *   GET /api/contact    →  already served by your existing ContactController
 *   These two are fetched by fetchDashboardData() in analitics.html for the
 *   KPI cards (Total Customers, Inbox Messages) and require NO changes.
 *
 * File location:
 *   src/main/java/com/realestate/project/controller/AnalyticsController.java
 */
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")   // allows the HTML frontend running on XAMPP to reach the API
public class AnalyticsController {

    private final TransactionService transactionService;

    public AnalyticsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /* ── GET ALL ───────────────────────────────────────────── */

    /**
     * GET /api/transactions
     * Called on page load to populate the "Recent Transactions" table.
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> list = transactionService.getAllTransactions();
        return ResponseEntity.ok(list);
    }

    /* ── GET ONE ───────────────────────────────────────────── */

    /**
     * GET /api/transactions/{id}
     * Useful for pre-filling the edit modal from the server side.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ── CREATE ────────────────────────────────────────────── */

    /**
     * POST /api/transactions
     * Body (JSON): { "property": "...", "client": "...", "value": "...", "status": "Sold" }
     * Triggered by saveRecord() in analitics.html when editingId is null.
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionDTO dto) {
        Transaction created = transactionService.createTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /* ── UPDATE ────────────────────────────────────────────── */

    /**
     * PUT /api/transactions/{id}
     * Body (JSON): { "property": "...", "client": "...", "value": "...", "status": "Pending" }
     * Triggered by saveRecord() in analitics.html when editingId is set.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionDTO dto) {

        return transactionService.updateTransaction(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ── DELETE ────────────────────────────────────────────── */

    /**
     * DELETE /api/transactions/{id}
     * Triggered by confirmDelete() → the trash icon in the table row.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        boolean deleted = transactionService.deleteTransaction(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
