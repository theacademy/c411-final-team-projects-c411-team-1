package com.mthree.etrade.service;

import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.StockPrice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface StockService {

    Stock getStock(String symbol);

    List<Stock> getAllStocks();

    BigDecimal getCurrentPrice(String symbol);

    void updateStock(Stock stock);

    void addStock(Stock stock);

    void deleteStock(String symbol);

    List<Stock> searchStock(String keyword);

    List<StockPrice> getStockHistory(String symbol, LocalDate startDate, LocalDate endDate);
}
