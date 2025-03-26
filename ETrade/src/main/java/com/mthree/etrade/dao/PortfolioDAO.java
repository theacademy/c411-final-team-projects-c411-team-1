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
public interface PortfolioDAO extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUserId(Long userId);
    Portfolio findByName(String name);
    //Total Value Query, based on quantity * avg buy price
    @Query("SELECT SUM(sp.quantity * sp.averageBuyPrice) FROM StockPortfolio sp WHERE sp.portfolio.portfolioId = :portfolioId")
    BigDecimal getPortfolioTotalValue(@Param("portfolioId") Long portfolioId);

    Optional<StockPortfolio> findStockInPortfolio(int portfolioId, String stockSymbol);
}
