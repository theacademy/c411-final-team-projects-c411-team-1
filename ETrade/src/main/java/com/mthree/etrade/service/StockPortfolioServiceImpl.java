package com.mthree.etrade.service;

import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.StockPortfolioId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class StockPortfolioServiceImpl implements StockPortfolioService {

    private final StockPortfolioDao stockPortfolioDao;
    private final PortfolioDao portfolioDAO;

    @Autowired
    public StockPortfolioServiceImpl(StockPortfolioDao stockPortfolioDao, PortfolioDao portfolioDAO) {
        this.stockPortfolioDao = stockPortfolioDao;
        this.portfolioDAO = portfolioDAO;
    }

    @Override
    public StockPortfolio addStockToPortfolio(StockPortfolio stockPortfolio) {
        return stockPortfolioDao.save(stockPortfolio);
    }

    @Override
    public StockPortfolio updateStockInPortfolio(StockPortfolioId id, StockPortfolio stockPortfolio) {
        Optional<StockPortfolio> existing = stockPortfolioDao.findById(id);
        if (existing.isPresent()) {
            StockPortfolio toUpdate = existing.get();
            toUpdate.setQuantity(stockPortfolio.getQuantity());
            toUpdate.setAvgBuyPrice(stockPortfolio.getAvgBuyPrice());
            return stockPortfolioDao.save(toUpdate);
        } else {
            throw new IllegalArgumentException("StockPortfolio not found for update");
        }
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
        return stockPortfolioDao.findById_PortfolioId(portfolioId);
    }

    @Override
    public List<StockPortfolio> getPortfolioByUserId(Long userId) {
        return stockPortfolioDao.findByPortfolio_User_Id(userId);
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
        return stockPortfolioDao.findById_PortfolioIdAndId_StockSymbol(portfolioId, stockSymbol).isPresent();
    }
}
