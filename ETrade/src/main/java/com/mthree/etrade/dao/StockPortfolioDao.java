package com.mthree.etrade.dao;

import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.StockPortfolioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockPortfolioDao extends JpaRepository<StockPortfolio, StockPortfolioId> {

    // Find all stock entries for a specific portfolio
    @Query("Select sp from StockPortfolio sp where sp.portfolio.portfolioId = :portfolioId")
    List<StockPortfolio> findByPortfolioId(@Param("portfolioId") Long portfolioId);

    // Find all stock entries for a user via nested relationship
    @Query("Select sp from StockPortfolio sp where sp.portfolio.user.id = :userId")
    List<StockPortfolio> findByPortfolioUserId(@Param("userId") Long userId);

    // Find a specific stock entry by portfolio and stock
    @Query("Select sp from StockPortfolio sp where sp.portfolio.portfolioId = :portfolioId and sp.stock.symbol = :stockSymbol")
    Optional<StockPortfolio> findByPortfolioIdAndStockSymbol(@Param("portfolioId") Long portfolioId, @Param("stockSymbol") String stockSymbol);

    // Delete a specific stock from a portfolio
    @Transactional
    @Modifying
    @Query("delete from StockPortfolio sp where sp.portfolio.portfolioId = :portfolioId and sp.stock.symbol = :stockSymbol")
    void deleteByPortfolioIdAndStockSymbol(@Param("portfolioId") Long portfolioId, @Param("stockSymbol") String stockSymbol);

    // Optional: Get all entries by stock
    @Query("Select sp from StockPortfolio sp where sp.stock.symbol = :stockSymbol")
    List<StockPortfolio> findByStockSymbol(@Param("stockSymbol") String stockSymbol);
}
