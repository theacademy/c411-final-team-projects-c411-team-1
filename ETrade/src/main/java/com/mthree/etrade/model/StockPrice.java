package com.mthree.etrade.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class StockPrice {

    private Date date;
    private BigDecimal price;
    private long volume;
    private BigDecimal change;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    @Override
    public String toString() {
        return "StockPrice{" +
                "date=" + date +
                ", price=" + price +
                ", volume=" + volume +
                ", change=" + change +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockPrice that = (StockPrice) o;
        return volume == that.volume && Objects.equals(date, that.date) && Objects.equals(price, that.price) && Objects.equals(change, that.change);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, price, volume, change);
    }
}
