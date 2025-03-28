package com.mthree.etrade.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolioId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal total;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Ensures createdAt is set automatically before inserting
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("portfolio")
    private List<StockPortfolio> stockPortfolios;

    public Portfolio() {}

    public Portfolio(User user, String name, String description, BigDecimal total) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.total = total;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<StockPortfolio> getStockPortfolios() {
        return stockPortfolios;
    }

    public void setStockPortfolios(List<StockPortfolio> stockPortfolios) {
        this.stockPortfolios = stockPortfolios;
    }

    // Automatically update the `updatedAt` field when modifying the portfolio
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Portfolio portfolio = (Portfolio) o;

        return portfolioId != null && portfolioId.equals(portfolio.getPortfolioId());
    }

    @Override
    public int hashCode() {
        return portfolioId != null ? portfolioId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "portfolioId=" + portfolioId +
                //", user=" + (user != null ? user.getId() : null) +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", total=" + total +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

