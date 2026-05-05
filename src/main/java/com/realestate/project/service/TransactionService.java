package com.realestate.project.service;

import com.realestate.project.dto.TransactionDTO;
import com.realestate.project.model.Transaction;
import com.realestate.project.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Transaction CRUD operations.
 * Called by AnalyticsController to keep business logic out of the controller.
 *
 * File location:
 *   src/main/java/com/realestate/project/service/TransactionService.java
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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
                dto.getProperty(),
                dto.getClient(),
                dto.getValue(),
                dto.getStatus()
        );
        return transactionRepository.save(tx);
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
            existing.setProperty(dto.getProperty());
            existing.setClient(dto.getClient());
            existing.setValue(dto.getValue());
            existing.setStatus(dto.getStatus());
            return transactionRepository.save(existing);
        });
    }

    /* ── DELETE ────────────────────────────────────────────── */

    /**
     * Deletes a transaction by ID.
     *
     * @return true if deleted, false if ID was not found
     */
    public boolean deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
