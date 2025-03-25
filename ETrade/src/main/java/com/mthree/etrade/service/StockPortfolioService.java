package com.mthree.etrade.service;

import com.mthree.etrade.model.StockPortfolio;

import java.util.List;

public interface StockPortfolioService {
    StockPortfolio addStockToPortfolio(StockPortfolio stockPortfolio);

    StockPortfolio updateStockInPortfolio(Long id, StockPortfolio stockPortfolio);

    void removeStockFromPortfolio(Long id);

    List<StockPortfolio> getPortfolioByUserId(Long userId);

    double calculatePortfolioValue(Long userId);
}