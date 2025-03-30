package com.mthree.etrade.service;

import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.model.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioDao portfolioDAO;

    @Autowired
    public PortfolioServiceImpl(PortfolioDao portfolioDAO) {
        this.portfolioDAO = portfolioDAO;
    }

    @Override
    public Portfolio savePortfolio(Portfolio portfolio) {
        portfolio.setUpdatedAt(LocalDateTime.now()); //try commenting out this line and running frontend again
        return portfolioDAO.save(portfolio);
    }

    @Override
    public Portfolio getPortfolioById(Long id) {
        return portfolioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + id));
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
        return portfolioDAO.findAll();
    }

    @Override
    public List<Portfolio> getPortfoliosByUserId(Long userId) {
        return portfolioDAO.findByUserId(userId);
    }

    @Override
    public void deletePortfolioById(Long id) {
        if (!portfolioDAO.existsById(id)) {
            throw new RuntimeException("Cannot delete. Portfolio with ID " + id + " does not exist.");
        }
        portfolioDAO.deleteById(id);
    }

    @Override
    public BigDecimal calculateTotalValue(Long portfolioId) {
        return portfolioDAO.getPortfolioTotalValue(portfolioId);
    }

    // TODO: Implement performanceMetrics(int portfolioId)
}
