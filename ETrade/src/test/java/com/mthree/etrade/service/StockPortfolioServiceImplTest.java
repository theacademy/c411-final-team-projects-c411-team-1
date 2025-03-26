package com.mthree.etrade.service;

import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.StockPortfolioId;
import com.mthree.etrade.model.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

public class StockPortfolioServiceImplTest {

    private StockPortfolioDao stockPortfolioDao;
    private PortfolioDao portfolioDao;
    private StockPortfolioServiceImpl stockPortfolioService;

    private Stock stock;
    private Portfolio portfolio;
    private StockPortfolio stockPortfolio;
    private StockPortfolioId stockPortfolioId;

    @BeforeEach
    public void setup() {
        stockPortfolioDao = mock(StockPortfolioDao.class);
        portfolioDao = mock(PortfolioDao.class);
        stockPortfolioService = new StockPortfolioServiceImpl(stockPortfolioDao, portfolioDao);

        portfolio = new Portfolio();
        portfolio.setPortfolioId(1L);

        stock = new Stock();
        stock.setSymbol("AAPL");

        stockPortfolioId = new StockPortfolioId(1L, "AAPL");

        stockPortfolio = new StockPortfolio();
        stockPortfolio.setPortfolio(portfolio);
        stockPortfolio.setStock(stock);
        stockPortfolio.setQuantity(10);
        stockPortfolio.setAvgBuyPrice(new BigDecimal("150.00"));
    }

    @Test
    public void testAddStockToPortfolio() {
        when(stockPortfolioDao.save(any(StockPortfolio.class))).thenReturn(stockPortfolio);
        StockPortfolio saved = stockPortfolioService.addStockToPortfolio(stockPortfolio);
        assertNotNull(saved);
        assertEquals(stockPortfolio.getQuantity(), saved.getQuantity());
        verify(stockPortfolioDao).save(stockPortfolio);
    }

    @Test
    public void testUpdateStockInPortfolio() {
        when(stockPortfolioDao.findById(stockPortfolioId)).thenReturn(Optional.of(stockPortfolio));
        stockPortfolio.setQuantity(20);
        StockPortfolio updated = stockPortfolioService.updateStockInPortfolio(stockPortfolioId, stockPortfolio);
        assertEquals(20, updated.getQuantity());
        verify(stockPortfolioDao).save(stockPortfolio);
    }

    @Test
    public void testRemoveStockFromPortfolio() {
        stockPortfolioService.removeStockFromPortfolio(stockPortfolioId);
        verify(stockPortfolioDao).deleteById(stockPortfolioId);
    }

    @Test
    public void testGetStockPortfolioById() {
        when(stockPortfolioDao.findById(stockPortfolioId)).thenReturn(Optional.of(stockPortfolio));
        StockPortfolio found = stockPortfolioService.getStockPortfolioById(stockPortfolioId);
        assertEquals(stockPortfolio, found);
    }

    @Test
    public void testGetStocksByPortfolioId() {
        when(stockPortfolioDao.findById_PortfolioId(1L)).thenReturn(List.of(stockPortfolio));
        List<StockPortfolio> list = stockPortfolioService.getStocksByPortfolioId(1L);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetPortfolioByUserId() {
        when(stockPortfolioDao.findByPortfolio_User_Id(99L)).thenReturn(List.of(stockPortfolio));
        List<StockPortfolio> list = stockPortfolioService.getPortfolioByUserId(99L);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetAllStockPortfolios() {
        when(stockPortfolioDao.findAll()).thenReturn(List.of(stockPortfolio));
        assertFalse(stockPortfolioService.getAllStockPortfolios().isEmpty());
    }

    @Test
    public void testCalculatePortfolioValue() {
        when(stockPortfolioDao.findByPortfolio_User_Id(99L)).thenReturn(List.of(stockPortfolio));
        double value = stockPortfolioService.calculatePortfolioValue(99L);
        assertEquals(1500.0, value);
    }

    @Test
    public void testExistsByPortfolioAndStock() {
        when(stockPortfolioDao.findById_PortfolioIdAndId_StockSymbol(1L, "AAPL")).thenReturn(Optional.of(stockPortfolio));
        assertTrue(stockPortfolioService.existsByPortfolioAndStock(1L, "AAPL"));
    }
}

