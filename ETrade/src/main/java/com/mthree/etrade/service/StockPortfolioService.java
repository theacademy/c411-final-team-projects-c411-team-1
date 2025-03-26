package com.mthree.etrade.service;

import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.StockPortfolioId;

import java.util.List;

public interface StockPortfolioService {
    StockPortfolio addStockToPortfolio(StockPortfolio stockPortfolio);
    StockPortfolio updateStockInPortfolio(StockPortfolioId id, StockPortfolio stockPortfolio);
    void removeStockFromPortfolio(StockPortfolioId id);

    StockPortfolio getStockPortfolioById(StockPortfolioId id);
    List<StockPortfolio> getStocksByPortfolioId(Long portfolioId);
    List<StockPortfolio> getPortfolioByUserId(Long userId);

    List<StockPortfolio> getAllStockPortfolios();

    double calculatePortfolioValue(Long userId);

    boolean existsByPortfolioAndStock(Long portfolioId, String stockSymbol);
}
