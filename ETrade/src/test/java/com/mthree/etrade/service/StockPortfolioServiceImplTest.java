package com.mthree.etrade.service;

import com.mthree.etrade.TestApplicationConfiguration;
import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.dao.StockPortfolioDao;
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
@Import(StockPortfolioServiceImpl.class) // Import the actual service

public class StockPortfolioServiceImplTest {

    @Autowired
    private StockPortfolioDao stockPortfolioDao;

    @Autowired
    private PortfolioDao portfolioDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StockPortfolioServiceImpl stockPortfolioService;

    @Autowired
    private StockService stockService;

    @Autowired
    private PortfolioService portfolioService;

    private Portfolio portfolio;
    private Stock stock;

    @BeforeEach
    void setup() {
        // Save Portfolio
        User user = new User();
        user.setEmail("test@user.com");
        user.setPassword("test");
        user.setBalance(new BigDecimal("1000"));
        userDao.save(user);

        portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setName("Test Portfolio");
        portfolio.setDescription("Test Desc");
        portfolio.setTotal(BigDecimal.ZERO);
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolioDao.save(portfolio);

        // Save Stock
        stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setCompanyName("Apple Inc");
        stockService.addStock(stock);
        stockPortfolioDao.save(new StockPortfolio(portfolio, stock, 10, BigDecimal.valueOf(150)));
    }

    @AfterEach
    void tearDown() {
        List<StockPortfolio> stockPortfolios = stockPortfolioService.getAllStockPortfolios();
        for(StockPortfolio sp : stockPortfolios) {
            StockPortfolioId spI = new StockPortfolioId(sp.getPortfolio().getPortfolioId(), sp.getStock().getSymbol());
            stockPortfolioService.removeStockFromPortfolio(spI);
        }

        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        for(Portfolio p : portfolios) {
            portfolioService.deletePortfolioById(p.getPortfolioId());
        }

        List<Stock> stocks = stockService.getAllStocks();
        for(Stock s : stocks) {
            stockService.deleteStock(s.getSymbol());
        }

        List<User> users = userDao.findAll();
        for(User u : users) {
            userDao.deleteById(u.getId());
        }
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
        StockPortfolio update = new StockPortfolio();
        update.setQuantity(30);
        update.setAvgBuyPrice(BigDecimal.valueOf(160));
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
