package com.mthree.etrade.service;

import com.mthree.etrade.dao.PortfolioDAO;
import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.model.Portfolio;
import com.mthree.etrade.model.StockPortfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockPortfolioServiceImpl implements StockPortfolioService {

    @Autowired
    private StockPortfolioDao stockPortfolioDao;

    @Autowired
    private PortfolioDAO portfolioDAO;

    @Override
    public StockPortfolio addStockToPortfolio(StockPortfolio stockPortfolio) {
        return stockPortfolioDao.save(stockPortfolio);
    }

    @Override
    public StockPortfolio updateStockInPortfolio(Long id, StockPortfolio updatedStockPortfolio) {
        Optional<StockPortfolio> existingOpt = stockPortfolioDao.findById(id);
        if (existingOpt.isPresent()) {
            StockPortfolio existing = existingOpt.get();
            existing.setQuantity(updatedStockPortfolio.getQuantity());
            existing.setAvgBuyPrice(updatedStockPortfolio.getAvgBuyPrice());
            return stockPortfolioDao.save(existing);
        } else {
            throw new IllegalArgumentException("StockPortfolio not found with ID: " + id);
        }
    }

    @Override
    public void removeStockFromPortfolio(Long id) {
        stockPortfolioDao.deleteById(id);
    }

    @Override
    public StockPortfolio getStockPortfolioById(Long id) {
        return stockPortfolioDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StockPortfolio not found with ID: " + id));
    }

    @Override
    public List<StockPortfolio> getStocksByPortfolioId(Long portfolioId) {
        return stockPortfolioDao.findByPortfolio_PortfolioId(portfolioId);
    }

    @Override
    public List<StockPortfolio> getPortfolioByUserId(Long userId) {
        List<Portfolio> userPortfolios = portfolioDAO.findByUserId(userId);
        return userPortfolios.stream()
                .flatMap(p -> stockPortfolioDao.findByPortfolio_PortfolioId(p.getPortfolioId()).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<StockPortfolio> getAllStockPortfolios() {
        return stockPortfolioDao.findAll();
    }

    @Override
    public double calculatePortfolioValue(Long userId) {
        List<StockPortfolio> holdings = getPortfolioByUserId(userId);
        return holdings.stream()
                .mapToDouble(sp -> sp.getAvgBuyPrice().multiply(BigDecimal.valueOf(sp.getQuantity())).doubleValue())
                .sum();
    }

    @Override
    public boolean existsByPortfolioAndStock(Long portfolioId, String stockSymbol) {
        // Fixed the incomplete method
        return stockPortfolioDao.findByPortfolio_PortfolioIdAndStock_Symbol(portfolioId, stockSymbol).isPresent();
    }
}