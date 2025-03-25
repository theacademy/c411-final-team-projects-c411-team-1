package com.mthree.etrade.service;

import com.mthree.etrade.model.Stock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface StockService {

    Stock getStock(String symbol);

    List<Stock> getAllStocks();

    BigDecimal getCurrentPrice(String symbol);

    void updateStock(Stock stock);

    List<Stock> searchStockByName(String name, String exchange);

    List<BigDecimal> getStockHistory(String symbol, Date startDate, Date endDate);
}
