package com.mthree.etrade.service;

import com.mthree.etrade.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Transaction operations
 */
public interface TransactionService {

    /**
     * Find transaction by ID
     * @param id The transaction ID
     * @return The transaction if found
     */
    Transaction findById(int id);

    /**
     * Get all transactions
     * @return List of all transactions
     */
    List<Transaction> findAll();

    /**
     * Get transactions for a specific portfolio
     * @param portfolioId The portfolio ID
     * @return List of transactions for the portfolio
     */
    List<Transaction> findByPortfolioId(int portfolioId);

    /**
     * Get transactions for a specific user
     * @param userId The user ID
     * @return List of transactions for the user
     */
    List<Transaction> findByUserId(int userId);

    /**
     * Get transactions for a specific stock
     * @param stockSymbol The stock symbol
     * @return List of transactions for the stock
     */
    List<Transaction> findByStockSymbol(String stockSymbol);

    /**
     * Get transactions by type (BUY or SELL)
     * @param type The transaction type
     * @return List of transactions of the specified type
     */
    List<Transaction> findByTransactionType(String type);

    /**
     * Get transactions in a date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of transactions in the date range
     */
    List<Transaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Execute a buy transaction
     * @param portfolioId The portfolio ID
     * @param stockSymbol The stock symbol
     * @param quantity The quantity to buy
     * @param price The price per share
     * @return The created transaction
     */
    Transaction executeBuyTransaction(int portfolioId, String stockSymbol, int quantity, BigDecimal price);

    /**
     * Execute a sell transaction
     * @param portfolioId The portfolio ID
     * @param stockSymbol The stock symbol
     * @param quantity The quantity to sell
     * @param price The price per share
     * @return The created transaction
     */
    Transaction executeSellTransaction(int portfolioId, String stockSymbol, int quantity, BigDecimal price);

    /**
     * Save a transaction
     * @param transaction The transaction to save
     * @return The saved transaction
     */
    Transaction saveTransaction(Transaction transaction);

    /**
     * Delete a transaction
     * @param id The transaction ID
     */
    void deleteTransaction(int id);

    /**
     * Validate a transaction
     * @param portfolioId The portfolio ID
     * @param stockSymbol The stock symbol
     * @param quantity The quantity
     * @param price The price per share
     * @param transactionType The transaction type (BUY or SELL)
     * @return true if valid, false otherwise
     */
    boolean validateTransaction(int portfolioId, String stockSymbol, int quantity, BigDecimal price, String transactionType);
}