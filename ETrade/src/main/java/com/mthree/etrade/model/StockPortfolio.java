package com.mthree.etrade.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class StockPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String portfolioName;

    private Double totalValue;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StockHolding> stockHoldings = new HashSet<>();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<StockHolding> getStockHoldings() {
        return stockHoldings;
    }

    public void setStockHoldings(Set<StockHolding> stockHoldings) {
        this.stockHoldings = stockHoldings;
    }

    @Override
    public String toString() {
        return "StockPortfolio{" +
                "id=" + id +
                ", portfolioName='" + portfolioName + '\'' +
                ", totalValue=" + totalValue +
                ", user=" + user +
                ", stockHoldings=" + stockHoldings +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, portfolioName, totalValue, user, stockHoldings);

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StockPortfolio)) return false;
        StockPortfolio that = (StockPortfolio) o;
        return Objects.equals(id, that.id) && Objects.equals(portfolioName, that.portfolioName) && Objects.equals(totalValue, that.totalValue) && Objects.equals(user, that.user) && Objects.equals(stockHoldings, that.stockHoldings);
    }
}


