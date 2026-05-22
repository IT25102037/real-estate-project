package com.realestate.project.repository;

import com.realestate.project.model.Transaction;
import com.realestate.project.model.Transaction.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Transaction entities.
 * JpaRepository already provides: findAll, findById, save, deleteById, count.
 *
 * File path:
 *   src/main/java/com/realestate/project/repository/TransactionRepository.java
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /** Filter transactions by status — e.g. fetch all "Sold" records. */
    List<Transaction> findByStatus(TransactionStatus status);

    /** Fetch transactions for a specific client name. */
    List<Transaction> findByClientContainingIgnoreCase(String client);

    /** Fetch transactions for a specific property name. */
    List<Transaction> findByPropertyContainingIgnoreCase(String property);
}
