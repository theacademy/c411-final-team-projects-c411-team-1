package com.mthree.etrade.dao;

import com.mthree.etrade.model.StockPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPortfolioDao extends JpaRepository<StockPortfolio, Long> {
    List<StockPortfolio> findByUserId(Long userId);
}
