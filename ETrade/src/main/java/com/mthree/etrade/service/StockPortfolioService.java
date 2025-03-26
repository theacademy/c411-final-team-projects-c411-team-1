package com.mthree.etrade.service;

import com.mthree.etrade.model.StockPortfolio;

import java.util.List;

public interface StockPortfolioService {
    StockPortfolio addStockToPortfolio(StockPortfolio stockPortfolio);
    StockPortfolio updateStockInPortfolio(Long id, StockPortfolio stockPortfolio);
    void removeStockFromPortfolio(Long id);

    StockPortfolio getStockPortfolioById(Long id);
    List<StockPortfolio> getStocksByPortfolioId(Long portfolioId);
    List<StockPortfolio> getPortfolioByUserId(Long userId);
    //Get All Records (useful for admin)
    List<StockPortfolio> getAllStockPortfolios();

    double calculatePortfolioValue(Long userId);
    //Method for checking existence (to prevent duplicate symbols in a portfolio):
    boolean existsByPortfolioAndStock(Long portfolioId, String stockSymbol);
}

