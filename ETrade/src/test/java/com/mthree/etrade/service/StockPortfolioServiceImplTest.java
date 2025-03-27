package com.mthree.etrade.service;

import com.mthree.etrade.TestApplicationConfiguration;
import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.dao.StockDao;
import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.dao.TransactionDao;
import com.mthree.etrade.dao.UserDao;
import com.mthree.etrade.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class StockPortfolioServiceImplTest {

    @Autowired
    private StockPortfolioDao stockPortfolioDao;

    @Autowired
    private PortfolioDao portfolioDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private StockPortfolioService stockPortfolioService;

    private Portfolio portfolio;
    private Stock stock;

    @BeforeEach
    void setup() {
        // Clean database in proper order
        cleanupDatabase();

        // Create test data
        User user = new User();
        user.setEmail("test@user.com");
        user.setPassword("test");
        user.setName("Test User");
        user.setBalance(new BigDecimal("1000"));
        userDao.save(user);

        portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setName("Test Portfolio");
        portfolio.setDescription("Test Desc");
        portfolio.setTotal(BigDecimal.ZERO);
        portfolio.setCreatedAt(LocalDateTime.now());
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio = portfolioDao.save(portfolio);

        stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setCompanyName("Apple Inc");
        stockDao.save(stock);

        // Create a stock portfolio
        StockPortfolio sp = new StockPortfolio();
        sp.setPortfolio(portfolio);
        sp.setStock(stock);
        sp.setQuantity(10);
        sp.setAvgBuyPrice(BigDecimal.valueOf(150));
        sp.setLastUpdated(LocalDateTime.now());
        stockPortfolioDao.save(sp);
    }

    @AfterEach
    void tearDown() {
        cleanupDatabase();
    }

    private void cleanupDatabase() {
        // Clean up in the correct order to avoid constraint violations
        if (transactionDao != null) {
            transactionDao.deleteAll();
        }

        stockPortfolioDao.deleteAll();
        portfolioDao.deleteAll();
        stockDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    void testAddStockToPortfolio() {
        // Create a new stock
        Stock newStock = new Stock();
        newStock.setSymbol("MSFT");
        newStock.setCompanyName("Microsoft Corp");
        stockDao.save(newStock);

        // Create a new stock portfolio entry
        StockPortfolio stockPortfolio = new StockPortfolio();
        stockPortfolio.setPortfolio(portfolio);
        stockPortfolio.setStock(newStock);
        stockPortfolio.setQuantity(20);
        stockPortfolio.setAvgBuyPrice(BigDecimal.valueOf(145));
        stockPortfolio.setLastUpdated(LocalDateTime.now());

        StockPortfolio saved = stockPortfolioService.addStockToPortfolio(stockPortfolio);

        assertNotNull(saved);
        assertEquals(20, saved.getQuantity());
        assertEquals("MSFT", saved.getStock().getSymbol());
    }

    @Test
    void testGetStockPortfolioById() {
        StockPortfolioId id = new StockPortfolioId(portfolio.getPortfolioId(), stock.getSymbol());
        StockPortfolio result = stockPortfolioService.getStockPortfolioById(id);

        assertNotNull(result);
        assertEquals(stock.getSymbol(), result.getStock().getSymbol());
        assertEquals(portfolio.getPortfolioId(), result.getPortfolio().getPortfolioId());
    }

    @Test
    void testUpdateStockInPortfolio() {
        // Create the ID object with proper types
        StockPortfolioId id = new StockPortfolioId(portfolio.getPortfolioId(), stock.getSymbol());

        // Create the update object with new values
        StockPortfolio update = new StockPortfolio();
        update.setQuantity(30);
        update.setAvgBuyPrice(BigDecimal.valueOf(160));

        // Call the service method
        StockPortfolio updated = stockPortfolioService.updateStockInPortfolio(id, update);

        // Verify the update was successful
        assertNotNull(updated);
        assertEquals(30, updated.getQuantity());
        assertEquals(BigDecimal.valueOf(160).setScale(2), updated.getAvgBuyPrice().setScale(2));
    }

    @Test
    void testRemoveStockFromPortfolio() {
        StockPortfolioId id = new StockPortfolioId(portfolio.getPortfolioId(), stock.getSymbol());
        stockPortfolioService.removeStockFromPortfolio(id);

        // Verify it was removed
        List<StockPortfolio> portfolioStocks = stockPortfolioService.getStocksByPortfolioId(portfolio.getPortfolioId());
        assertTrue(portfolioStocks.isEmpty());
    }

    @Test
    void testGetAllStockPortfolios() {
        List<StockPortfolio> all = stockPortfolioService.getAllStockPortfolios();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }
}