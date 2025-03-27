
package com.mthree.etrade.service;

import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.model.Portfolio;
import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.StockPortfolioId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class StockPortfolioServiceImplTest {

    @Mock
    private StockPortfolioDao stockPortfolioDao;

    @Mock
    private PortfolioDao portfolioDAO;

    @InjectMocks
    private StockPortfolioServiceImpl stockPortfolioService;

    private StockPortfolio stockPortfolio;
    private StockPortfolioId stockPortfolioId;

    @BeforeEach
    void setUp() {
        Portfolio portfolio = new Portfolio();
        portfolio.setPortfolioId(1L);

        Stock stock = new Stock();
        stock.setSymbol("AAPL");

        stockPortfolioId = new StockPortfolioId(1L, "AAPL");
        stockPortfolio = new StockPortfolio();
        stockPortfolio.setPortfolio(portfolio);
        stockPortfolio.setStock(stock);
        stockPortfolio.setQuantity(10);
        stockPortfolio.setAvgBuyPrice(new BigDecimal("150.00"));
    }

    @Test
    void testAddStockToPortfolio() {
        when(stockPortfolioDao.save(any(StockPortfolio.class))).thenReturn(stockPortfolio);
        StockPortfolio saved = stockPortfolioService.addStockToPortfolio(stockPortfolio);
        assertNotNull(saved);
        assertEquals(stockPortfolio.getQuantity(), saved.getQuantity());
    }

    @Test
    void testUpdateStockInPortfolio() {
        when(stockPortfolioDao.findById(stockPortfolioId)).thenReturn(Optional.of(stockPortfolio));
        when(stockPortfolioDao.save(any(StockPortfolio.class))).thenReturn(stockPortfolio);
        StockPortfolio updated = stockPortfolioService.updateStockInPortfolio(stockPortfolioId, stockPortfolio);
        assertNotNull(updated);
    }

    @Test
    void testRemoveStockFromPortfolio() {
        stockPortfolioService.removeStockFromPortfolio(stockPortfolioId);
        verify(stockPortfolioDao, times(1)).deleteById(stockPortfolioId);
    }

    @Test
    void testGetStockPortfolioById() {
        when(stockPortfolioDao.findById(stockPortfolioId)).thenReturn(Optional.of(stockPortfolio));
        StockPortfolio found = stockPortfolioService.getStockPortfolioById(stockPortfolioId);
        assertEquals(stockPortfolio.getPortfolio().getPortfolioId(), found.getPortfolio().getPortfolioId());
    }

    @Test
    void testGetStocksByPortfolioId() {
        when(stockPortfolioDao.findByPortfolioId(1L)).thenReturn(List.of(stockPortfolio));
        List<StockPortfolio> results = stockPortfolioService.getStocksByPortfolioId(1L);
        assertEquals(1, results.size());
    }

    @Test
    void testGetPortfolioByUserId() {
        when(stockPortfolioDao.findByPortfolioUserId(1L)).thenReturn(List.of(stockPortfolio));
        List<StockPortfolio> results = stockPortfolioService.getPortfolioByUserId(1L);
        assertEquals(1, results.size());
    }

    @Test
    void testGetAllStockPortfolios() {
        when(stockPortfolioDao.findAll()).thenReturn(Arrays.asList(stockPortfolio));
        List<StockPortfolio> all = stockPortfolioService.getAllStockPortfolios();
        assertFalse(all.isEmpty());
    }

    @Test
    void testCalculatePortfolioValue() {
        when(stockPortfolioDao.findByPortfolioUserId(1L)).thenReturn(List.of(stockPortfolio));
        double value = stockPortfolioService.calculatePortfolioValue(1L);
        assertEquals(1500.00, value);
    }

    @Test
    void testExistsByPortfolioAndStock() {
        when(stockPortfolioDao.findByPortfolioIdAndStockSymbol(1L, "AAPL")).thenReturn(Optional.of(stockPortfolio));
        boolean exists = stockPortfolioService.existsByPortfolioAndStock(1L, "AAPL");
        assertTrue(exists);
    }
}
