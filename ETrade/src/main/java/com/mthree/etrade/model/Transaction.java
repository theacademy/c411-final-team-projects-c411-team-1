package com.mthree.etrade.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "stock_symbol", nullable = false)
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime date;

    @Column(name = "type", nullable = false, length = 5)
    private String transactionType; // "BUY" or "SELL"

    // Constructors
    public Transaction() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id &&
                quantity == that.quantity &&
                Objects.equals(stock, that.stock) &&
                Objects.equals(portfolio, that.portfolio) &&
                Objects.equals(price, that.price) &&
                Objects.equals(date, that.date) &&
                Objects.equals(transactionType, that.transactionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stock, portfolio, quantity, price, date, transactionType);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", stock=" + stock.getSymbol() +
                ", portfolio=" + portfolio.getPortfolioId() +
                ", quantity=" + quantity +
                ", price=" + price +
                ", date=" + date +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}