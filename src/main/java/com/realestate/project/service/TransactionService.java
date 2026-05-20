package com.realestate.project.service;

import com.realestate.project.dto.TransactionDTO;
import com.realestate.project.model.Transaction;
import com.realestate.project.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service layer for Transaction CRUD operations.
 * Called by AnalyticsController to keep business logic out of the controller.
 *
 * File location:
 *   src/main/java/com/realestate/project/service/TransactionService.java
 */
@Service
public class TransactionService {
    private static final Pattern MONEY_PATTERN = Pattern.compile(
            "^[Rr][Ss]\\.?\\s?(\\d{1,3}(?:,\\d{3})+|\\d+)(\\.\\d{1,2})?\\s?([KkMm])?$"
    );

    private final TransactionRepository transactionRepository;
    private final ActivityService activityService;

    public TransactionService(TransactionRepository transactionRepository, ActivityService activityService) {
        this.transactionRepository = transactionRepository;
        this.activityService = activityService;
    }

    /* ── READ ──────────────────────────────────────────────── */

    /** Returns all transactions (used by the analytics table). */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /** Returns a single transaction by ID, or empty if not found. */
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    /* ── CREATE ────────────────────────────────────────────── */

    /**
     * Creates a new transaction from the DTO payload sent by analitics.html.
     * POST /api/transactions
     */
    public Transaction createTransaction(TransactionDTO dto) {
        Transaction tx = new Transaction(
                dto.getProperty().trim(),
                dto.getClient().trim(),
                normalizeMoney(dto.getValue()),
                dto.getStatus()
        );
        Transaction saved = transactionRepository.save(tx);
        activityService.logActivity(
                "TRANSACTION_CREATED",
                "<strong>Sale closed</strong> — " + saved.getProperty() + " by " + saved.getClient() + " (" + saved.getValue() + ")",
                "banknote",
                "gold"
        );
        return saved;
    }

    /* ── UPDATE ────────────────────────────────────────────── */

    /**
     * Updates an existing transaction.
     * PUT /api/transactions/{id}
     *
     * @return updated transaction, or empty Optional if id not found
     */
    public Optional<Transaction> updateTransaction(Long id, TransactionDTO dto) {
        return transactionRepository.findById(id).map(existing -> {
            existing.setProperty(dto.getProperty().trim());
            existing.setClient(dto.getClient().trim());
            existing.setValue(normalizeMoney(dto.getValue()));
            existing.setStatus(dto.getStatus());
            Transaction saved = transactionRepository.save(existing);
            activityService.logActivity(
                    "TRANSACTION_UPDATED",
                    "<strong>Transaction updated</strong> — " + saved.getProperty() + " to " + saved.getStatus(),
                    "pencil",
                    "blue"
            );
            return saved;
        });
    }

    /* ── DELETE ────────────────────────────────────────────── */

    /**
     * Deletes a transaction by ID.
     *
     * @return true if deleted, false if ID was not found
     */
    public boolean deleteTransaction(Long id) {
        return transactionRepository.findById(id).map(tx -> {
            transactionRepository.delete(tx);
            activityService.logActivity(
                    "TRANSACTION_DELETED",
                    "<strong>Transaction deleted</strong> — " + tx.getProperty() + " (" + tx.getValue() + ")",
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
}
