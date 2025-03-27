package com.mthree.etrade.service;

import com.mthree.etrade.TestApplicationConfiguration;
import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.dao.StockDao;
import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.dao.UserDao;
import com.mthree.etrade.model.Portfolio;
import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class PortfolioServiceImplTest {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private PortfolioDao portfolioDao;

    @Autowired
    private StockPortfolioDao stockPortfolioDao;

    private User testUser;

    @BeforeEach
    void setUp() {
        stockPortfolioDao.deleteAll();
        portfolioDao.deleteAll();
        stockDao.deleteAll();
        userDao.deleteAll();

        testUser = new User();
        testUser.setName("alice");
        testUser.setEmail("alice@example.com");
        testUser.setPassword("secure123");
        testUser.setBalance(new BigDecimal("5000.00"));
        testUser = userDao.save(testUser);
    }

    @Test
    void testSaveAndGetPortfolio() {
        Portfolio portfolio = new Portfolio(testUser, "Retirement Fund", "Long-term growth", new BigDecimal("12000.00"));
        portfolio.setUpdatedAt(LocalDateTime.now());

        Portfolio saved = portfolioService.savePortfolio(portfolio);
        assertNotNull(saved.getPortfolioId());

        Portfolio fetched = portfolioService.getPortfolioById(saved.getPortfolioId());
        assertEquals("Retirement Fund", fetched.getName());
    }

    @Test
    void testGetAllPortfolios() {
        Portfolio p1 = new Portfolio(testUser, "Aggressive", "High risk", new BigDecimal("8000.00"));
        Portfolio p2 = new Portfolio(testUser, "Conservative", "Low risk", new BigDecimal("3000.00"));
        p1.setUpdatedAt(LocalDateTime.now());
        p2.setUpdatedAt(LocalDateTime.now());

        portfolioService.savePortfolio(p1);
        portfolioService.savePortfolio(p2);

        List<Portfolio> all = portfolioService.getAllPortfolios();
        assertEquals(2, all.size());
    }

    @Test
    void testGetPortfoliosByUserId() {
        Portfolio p1 = new Portfolio(testUser, "Tech", "All tech stocks", new BigDecimal("6000.00"));
        Portfolio p2 = new Portfolio(testUser, "Green Energy", "Clean tech", new BigDecimal("4000.00"));
        p1.setUpdatedAt(LocalDateTime.now());
        p2.setUpdatedAt(LocalDateTime.now());

        portfolioService.savePortfolio(p1);
        portfolioService.savePortfolio(p2);

        List<Portfolio> userPortfolios = portfolioService.getPortfoliosByUserId(testUser.getId());
        assertEquals(2, userPortfolios.size());
    }

    @Test
    void testDeletePortfolioById() {
        Portfolio portfolio = new Portfolio(testUser, "ToDelete", "Will be deleted", new BigDecimal("1000.00"));
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio = portfolioService.savePortfolio(portfolio);

        Long portfolioId = portfolio.getPortfolioId();
        portfolioService.deletePortfolioById(portfolioId);

        assertThrows(RuntimeException.class, () -> portfolioService.getPortfolioById(portfolioId));
    }

    @Test
    void testCalculateTotalValue() {
        // Create and save a stock
        Stock stock = new Stock();
        stock.setSymbol("TSLA");
        stock.setCompanyName("Tesla Inc.");
        stock = stockDao.save(stock);

        // Create and save a portfolio
        Portfolio portfolio = new Portfolio(testUser, "EV Portfolio", "Electric Vehicles", new BigDecimal("0.00"));
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio = portfolioService.savePortfolio(portfolio);

        // Link via StockPortfolio
        StockPortfolio sp = new StockPortfolio();
        sp.setPortfolio(portfolio);
        sp.setStock(stock);
        sp.setQuantity(5);
        sp.setAvgBuyPrice(new BigDecimal("200.00"));
        stockPortfolioDao.save(sp);

        // Test the total calculation
        BigDecimal total = portfolioService.calculateTotalValue(portfolio.getPortfolioId());
        assertEquals(new BigDecimal("1000.00"), total);
    }
}
