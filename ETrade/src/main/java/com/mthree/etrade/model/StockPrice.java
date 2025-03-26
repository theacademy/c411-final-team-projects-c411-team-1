package com.mthree.etrade.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class StockPrice {

    private LocalDate date;
    private BigDecimal close;
    private long volume;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockPrice that = (StockPrice) o;
        return volume == that.volume && Objects.equals(date, that.date) && Objects.equals(close, that.close) && Objects.equals(open, that.open) && Objects.equals(high, that.high) && Objects.equals(low, that.low);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, close, volume, open, high, low);
    }

    @Override
    public String toString() {
        return "StockPrice{" +
                "date=" + date +
                ", close=" + close +
                ", volume=" + volume +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                '}';
    }
}
