package com.mthree.etrade.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @Column(name = "stock_symbol", nullable = false)
    private String symbol;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(symbol, stock.symbol) && Objects.equals(companyName, stock.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, companyName);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
