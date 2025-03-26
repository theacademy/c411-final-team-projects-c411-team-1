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
    List<StockPortfolio> findById_PortfolioId(Long portfolioId);

    // Find all stock entries for a user via nested relationship
    List<StockPortfolio> findByPortfolio_User_Id(Long userId);

    // Find a specific stock entry by portfolio and stock
    Optional<StockPortfolio> findById_PortfolioIdAndId_StockSymbol(Long portfolioId, String stockSymbol);

    // Delete a specific stock from a portfolio
    void deleteById_PortfolioIdAndId_StockSymbol(Long portfolioId, String stockSymbol);

    // Optional: Get all entries by stock
    List<StockPortfolio> findById_StockSymbol(String stockSymbol);
}
