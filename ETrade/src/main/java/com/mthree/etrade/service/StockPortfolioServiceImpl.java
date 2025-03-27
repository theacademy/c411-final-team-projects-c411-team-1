package com.mthree.etrade.service;

import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.StockPortfolioId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockPortfolioServiceImpl implements StockPortfolioService {

    private final StockPortfolioDao stockPortfolioDao;

    @Autowired
    public StockPortfolioServiceImpl(StockPortfolioDao stockPortfolioDao, PortfolioDao portfolioDAO) {
        this.stockPortfolioDao = stockPortfolioDao;
    }

    @Override
    public StockPortfolio addStockToPortfolio(StockPortfolio stockPortfolio) {
        return stockPortfolioDao.save(stockPortfolio);
    }

    @Override
    @Transactional
    public StockPortfolio updateStockInPortfolio(StockPortfolioId id, StockPortfolio updatedData) {
        StockPortfolio existing = stockPortfolioDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StockPortfolio not found for update"));

        // Only update mutable fields (do not touch ID-related references)
        existing.setQuantity(updatedData.getQuantity());
        existing.setAvgBuyPrice(updatedData.getAvgBuyPrice());
        existing.setLastUpdated(LocalDateTime.now());

        return stockPortfolioDao.save(existing);
    }

    @Override
    public void removeStockFromPortfolio(StockPortfolioId id) {
        stockPortfolioDao.deleteById(id);
    }

    @Override
    public StockPortfolio getStockPortfolioById(StockPortfolioId id) {
        return stockPortfolioDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StockPortfolio not found with given ID"));
    }

    @Override
    public List<StockPortfolio> getStocksByPortfolioId(Long portfolioId) {
        return stockPortfolioDao.findByPortfolioId(portfolioId);
    }

    @Override
    public List<StockPortfolio> getPortfolioByUserId(Long userId) {
        return stockPortfolioDao.findByPortfolioUserId(userId);
    }

    @Override
    public List<StockPortfolio> getAllStockPortfolios() {
        return stockPortfolioDao.findAll();
    }

    @Override
    public double calculatePortfolioValue(Long userId) {
        List<StockPortfolio> userStocks = getPortfolioByUserId(userId);
        return userStocks.stream()
                .mapToDouble(sp -> sp.getAvgBuyPrice().doubleValue() * sp.getQuantity())
                .sum();
    }

    @Override
    public boolean existsByPortfolioAndStock(Long portfolioId, String stockSymbol) {
        return stockPortfolioDao.findByPortfolioIdAndStockSymbol(portfolioId, stockSymbol).isPresent();
    }
}
