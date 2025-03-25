package com.mthree.etrade.dao;

import com.mthree.etrade.model.StockPortfolio;

import java.util.List;

public interface StockPortfolioDao {
    StockPortfolio getPortfolioById(int id);
    List<StockPortfolio> getAllPortfolios();
    StockPortfolio createPortfolio(StockPortfolio portfolio);
    void updatePortfolio(StockPortfolio portfolio);
    void deletePortfolio(int id);
}
