package com.mthree.etrade.dao;

import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.StockPortfolioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockPortfolioDao extends JpaRepository<StockPortfolio, StockPortfolioId> {

    // Find all stock entries for a specific portfolio
    List<StockPortfolio> findByPortfolio_PortfolioId(Long portfolioId);

    // Find a specific stock entry by portfolio and stock
    Optional<StockPortfolio> findByPortfolio_PortfolioIdAndStock_Symbol(Long portfolioId, String symbol);

    // Delete a specific stock from a portfolio
    void deleteByPortfolio_PortfolioIdAndStock_Symbol(Long portfolioId, String symbol);

    // Optional: Get all entries by stock
    List<StockPortfolio> findByStock_Symbol(String symbol);
}
