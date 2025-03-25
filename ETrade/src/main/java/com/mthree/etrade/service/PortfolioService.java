package com.mthree.etrade.service;

import com.mthree.etrade.model.Portfolio;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioService {

    Portfolio savePortfolio(Portfolio portfolio);
    Portfolio getPortfolioById(Long id);
    List<Portfolio> getAllPortfolios();
    List<Portfolio> getPortfoliosByUserId(Long userId);
    void deletePortfolioById(Long id);
    BigDecimal calculateTotalValue(Long portfolioId);

    // TODO: implement performanceMetrics(Long portfolioId) in stretch goal
}
