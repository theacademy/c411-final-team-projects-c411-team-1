package com.mthree.etrade.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_portfolio")
public class StockPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_symbol", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "average_buy_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal avgBuyPrice;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    public StockPortfolio() {
        this.lastUpdated = LocalDateTime.now();
    }

    public StockPortfolio(Portfolio portfolio, Stock stock, int quantity, BigDecimal avgBuyPrice) {
        this.portfolio = portfolio;
        this.stock = stock;
        this.quantity = quantity;
        this.avgBuyPrice = avgBuyPrice;
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAvgBuyPrice() {
        return avgBuyPrice;
    }

    public void setAvgBuyPrice(BigDecimal avgBuyPrice) {
        this.avgBuyPrice = avgBuyPrice;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "StockPortfolio{" +
                "id=" + id +
                ", portfolio=" + (portfolio != null ? portfolio.getPortfolioId() : null) +
                ", stock=" + (stock != null ? stock.getSymbol() : null) +
                ", quantity=" + quantity +
                ", avgBuyPrice=" + avgBuyPrice +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
