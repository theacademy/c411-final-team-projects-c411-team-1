package com.mthree.etrade.controller;

import com.mthree.etrade.model.Transaction;
import com.mthree.etrade.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * REST Controller for transaction operations
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Get all transactions
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Get transaction by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.findById(id);
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    /**
     * Get transactions by portfolio ID
     */
    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<Transaction>> getTransactionsByPortfolioId(@PathVariable Long portfolioId) {
        List<Transaction> transactions = transactionService.findByPortfolioId(portfolioId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Get transactions by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.findByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Get transactions by stock symbol
     */
    @GetMapping("/stock/{stockSymbol}")
    public ResponseEntity<List<Transaction>> getTransactionsByStockSymbol(@PathVariable String stockSymbol) {
        List<Transaction> transactions = transactionService.findByStockSymbol(stockSymbol);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Get transactions by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Transaction>> getTransactionsByType(@PathVariable String type) {
        List<Transaction> transactions = transactionService.findByTransactionType(type);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Get transactions by date range
     */
    @GetMapping("/dateRange")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Transaction> transactions = transactionService.findByDateRange(start, end);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Execute a buy transaction
     */
    @PostMapping("/buy")
    public ResponseEntity<Transaction> executeBuyTransaction(@RequestBody BuyTransactionRequest request) {
        try {
            Transaction transaction = transactionService.executeBuyTransaction(
                    request.getPortfolioId(),
                    request.getStockSymbol(),
                    request.getQuantity(),
                    request.getPrice()
            );
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Execute a sell transaction
     */
    @PostMapping("/sell")
    public ResponseEntity<Transaction> executeSellTransaction(@RequestBody SellTransactionRequest request) {
        try {
            Transaction transaction = transactionService.executeSellTransaction(
                    request.getPortfolioId(),
                    request.getStockSymbol(),
                    request.getQuantity(),
                    request.getPrice()
            );
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a transaction (admin only in a real app)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Request class for buying stocks
     */
    public static class BuyTransactionRequest {
        private Long portfolioId;
        private String stockSymbol;
        private int quantity;
        private BigDecimal price;

        // Getters and setters
        public Long getPortfolioId() { return portfolioId; }
        public void setPortfolioId(Long portfolioId) { this.portfolioId = portfolioId; }

        public String getStockSymbol() { return stockSymbol; }
        public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }

    /**
     * Request class for selling stocks
     */
    public static class SellTransactionRequest {
        private Long portfolioId;
        private String stockSymbol;
        private int quantity;
        private BigDecimal price;

        // Getters and setters
        public Long getPortfolioId() { return portfolioId; }
        public void setPortfolioId(Long portfolioId) { this.portfolioId = portfolioId; }

        public String getStockSymbol() { return stockSymbol; }
        public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }
}