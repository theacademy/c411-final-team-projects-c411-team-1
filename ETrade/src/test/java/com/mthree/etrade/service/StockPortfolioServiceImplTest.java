package com.mthree.etrade.service;

import com.mthree.etrade.TestApplicationConfiguration;
import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
@Import(StockPortfolioServiceImpl.class) // Import the actual service

public class StockPortfolioServiceImplTest {

    @Autowired
    private StockPortfolioDao stockPortfolioDao;

    @Autowired
    private PortfolioDao portfolioDao;

    @Autowired
    private StockPortfolioServiceImpl stockPortfolioService;

    private Portfolio portfolio;
    private Stock stock;

    @BeforeEach
    void setup() {
        // Save Portfolio
        User user = new User();
        user.setId(1L); // a user with ID 1 in test DB
        portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setName("Test Portfolio");
        portfolio.setDescription("Test Desc");
        portfolio.setTotal(BigDecimal.ZERO);
        portfolioDao.save(portfolio);

        // Save Stock
        stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setCompanyName("Apple Inc");
        stockPortfolioDao.save(new StockPortfolio(portfolio, stock, 10, BigDecimal.valueOf(150)));
    }

    @Test
    void testAddStockToPortfolio() {
        StockPortfolio stockPortfolio = new StockPortfolio(portfolio, stock, 20, BigDecimal.valueOf(145));
        StockPortfolio saved = stockPortfolioService.addStockToPortfolio(stockPortfolio);
        assertNotNull(saved);
        assertEquals(20, saved.getQuantity());
    }

    @Test
    void testGetStockPortfolioById() {
        StockPortfolioId id = new StockPortfolioId(portfolio.getPortfolioId(), stock.getSymbol());
        StockPortfolio result = stockPortfolioService.getStockPortfolioById(id);
        assertNotNull(result);
        assertEquals(stock.getSymbol(), result.getStock().getSymbol());
    }

    @Test
    void testUpdateStockInPortfolio() {
        StockPortfolioId id = new StockPortfolioId(portfolio.getPortfolioId(), stock.getSymbol());
        StockPortfolio update = new StockPortfolio(portfolio, stock, 30, BigDecimal.valueOf(160));
        StockPortfolio updated = stockPortfolioService.updateStockInPortfolio(id, update);

        assertNotNull(updated);
        assertEquals(30, updated.getQuantity());
        assertEquals(BigDecimal.valueOf(160), updated.getAvgBuyPrice());
    }

    @Test
    void testRemoveStockFromPortfolio() {
        StockPortfolioId id = new StockPortfolioId(portfolio.getPortfolioId(), stock.getSymbol());
        stockPortfolioService.removeStockFromPortfolio(id);
        assertFalse(stockPortfolioDao.findById(id).isPresent());
    }

    @Test
    void testGetAllStockPortfolios() {
        List<StockPortfolio> all = stockPortfolioService.getAllStockPortfolios();
        assertFalse(all.isEmpty());
    }
}
