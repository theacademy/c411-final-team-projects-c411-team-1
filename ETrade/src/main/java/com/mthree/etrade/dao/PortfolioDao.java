package com.mthree.etrade.dao;

import com.mthree.etrade.model.Portfolio;
import com.mthree.etrade.model.StockPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioDao extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUserId(Long userId);
    Portfolio findByName(String name);

    // Total Value Query, fixed field name to match StockPortfolio model
    @Query("SELECT SUM(sp.quantity * sp.avgBuyPrice) FROM StockPortfolio sp WHERE sp.portfolio.portfolioId = :portfolioId")
    BigDecimal getPortfolioTotalValue(@Param("portfolioId") Long portfolioId);

    // Removed redundant methods that should be in StockPortfolioDao
}