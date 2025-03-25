package com.mthree.etrade.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StockPortfolio {
    private int portfolioId;
    private int userId;
    private BigDecimal totalValue;
    private Map<String, Integer> stockHoldings = new HashMap<>();

    public int getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Map<String, Integer> getStockHoldings() {
        return stockHoldings;
    }

    public void setStockHoldings(Map<String, Integer> stockHoldings) {
        this.stockHoldings = stockHoldings;
    }

    public void addStock(String ticker, int quantity) {
        stockHoldings.put(ticker, stockHoldings.getOrDefault(ticker, 0) + quantity);
    }

    public void removeStock(String ticker, int quantity) {
        if (stockHoldings.containsKey(ticker)) {
            int updated = stockHoldings.get(ticker) - quantity;
            if (updated <= 0) stockHoldings.remove(ticker);
            else stockHoldings.put(ticker, updated);
        }
    }
}
