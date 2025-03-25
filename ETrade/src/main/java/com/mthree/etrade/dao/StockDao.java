package com.mthree.etrade.dao;

import com.mthree.etrade.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDao extends JpaRepository<Stock, String> {
}
