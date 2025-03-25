package com.mthree.etrade.dao;

import com.mthree.etrade.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PortfolioDAO extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUserId(Long userId);
    Portfolio findByName(String name);
}
