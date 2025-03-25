package com.etrade.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private Stock stock;
    private Portfolio portfolio;
    private int quantity;
    private BigDecimal price;
    private LocalDateTime transactionDate;
    private String type; // "BUY" or "SELL"

    // Constructors
    public Transaction() {
    }

    public Transaction(Stock stock, Portfolio portfolio, int quantity,
                       BigDecimal price, LocalDateTime transactionDate, String type) {
        this.stock = stock;
        this.portfolio = portfolio;
        this.quantity = quantity;
        this.price = price;
        this.transactionDate = transactionDate;
        this.type = type;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Utility methods
    public BigDecimal getTotalAmount() {
        return price.multiply(new BigDecimal(quantity));
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", stock=" + stock.getStockSymbol() +
                ", portfolio=" + portfolio.getPortfolioId() +
                ", quantity=" + quantity +
                ", price=" + price +
                ", transactionDate=" + transactionDate +
                ", type='" + type + '\'' +
                '}';
    }
}