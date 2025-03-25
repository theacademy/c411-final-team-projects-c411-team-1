package com.mthree.etrade.service;

import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.model.StockPortfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class StockPortfolioService {

    @Autowired
    private StockPortfolioDao portfolioDao;

    @Autowired
    private StockPriceService stockPriceService; // Assume this is implemented

    public StockPortfolio getPortfolio(int id) {
        StockPortfolio portfolio = portfolioDao.getPortfolioById(id);
        updatePortfolioValue(portfolio);
        return portfolio;
    }

    public StockPortfolio createPortfolio(StockPortfolio portfolio) {
        return portfolioDao.createPortfolio(portfolio);
    }

    public void updatePortfolio(StockPortfolio portfolio) {
        updatePortfolioValue(portfolio);
        portfolioDao.updatePortfolio(portfolio);
    }

    public void deletePortfolio(int id) {
        portfolioDao.deletePortfolio(id);
    }

    public void updatePortfolioValue(StockPortfolio portfolio) {
        BigDecimal totalValue = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : portfolio.getStockHoldings().entrySet()) {
            BigDecimal price = stockPriceService.getPrice(entry.getKey());
            totalValue = totalValue.add(price.multiply(BigDecimal.valueOf(entry.getValue())));
        }
        portfolio.setTotalValue(totalValue);
    }
}
