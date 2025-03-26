package com.mthree.etrade.model;

import java.io.Serializable;
import java.util.Objects;

public class StockPortfolioId implements Serializable {
    private Long portfolio;
    private String stock;

    public StockPortfolioId() {}

    public StockPortfolioId(Long portfolio, String stock) {
        this.portfolio = portfolio;
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockPortfolioId)) return false;
        StockPortfolioId that = (StockPortfolioId) o;
        return Objects.equals(portfolio, that.portfolio) &&
                Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portfolio, stock);
    }
}
