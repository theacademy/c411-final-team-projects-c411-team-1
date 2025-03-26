package com.mthree.etrade.dao;

import com.mthree.etrade.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Access Object interface for Transaction entity using Spring Data JPA
 */
@Repository
public interface TransactionDao extends JpaRepository<Transaction, Integer> {

    /**
     * Find transactions by portfolio ID
     */
    List<Transaction> findByPortfolioPortfolioId(int portfolioId);

    /**
     * Find transactions by stock symbol
     */
    List<Transaction> findByStockSymbol(String stockSymbol);

    /**
     * Find transactions by transaction type
     */
    List<Transaction> findByTransactionType(String type);

    /**
     * Find transactions in a date range
     */
    List<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find transactions by user ID (custom query)
     */
    @Query("SELECT t FROM Transaction t WHERE t.portfolio.portfolioOwner.id = :userId")
    List<Transaction> findByUserId(@Param("userId") int userId);

    /**
     * Find transactions by portfolio ID and stock symbol
     */
    List<Transaction> findByPortfolioPortfolioIdAndStockSymbol(int portfolioId, String stockSymbol);
}