package com.mthree.etrade.dao;

import com.mthree.etrade.TestApplicationConfiguration;
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
class PortfolioDaoTest {

    @Autowired
    PortfolioDao portfolioDao;

    @Autowired
    UserDao userDao;

    @Autowired
    StockDao stockDao;

    @Autowired
    StockPortfolioDao stockPortfolioDao;

    private User testUser;

    @BeforeEach
    void setUp() {
        stockPortfolioDao.deleteAll();
        portfolioDao.deleteAll();
        stockDao.deleteAll();
        userDao.deleteAll();

        testUser = new User();
        testUser.setName("johndoe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("password123");
        testUser.setBalance(new BigDecimal("10000.00"));
        testUser = userDao.save(testUser);
    }

    @Test
    void testAddFindAll() {
        Portfolio p1 = new Portfolio(testUser, "Tech Stocks", "Apple & friends", new BigDecimal("5000.00"));
        p1.setUpdatedAt(LocalDateTime.now());
        Portfolio p2 = new Portfolio(testUser, "Dividend Portfolio", "Steady income", new BigDecimal("3000.00"));
        p2.setUpdatedAt(LocalDateTime.now());

        p1 = portfolioDao.save(p1);
        p2 = portfolioDao.save(p2);

        List<Portfolio> all = portfolioDao.findAll();
        assertEquals(2, all.size(), "Should retrieve 2 portfolios");
        assertTrue(all.contains(p1));
        assertTrue(all.contains(p2));
    }

    @Test
    void testFindById() {
        Portfolio portfolio = new Portfolio(testUser, "Growth", "Growth-oriented stocks", new BigDecimal("8000.00"));
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio = portfolioDao.save(portfolio);

        Portfolio fromDao = portfolioDao.findById(portfolio.getPortfolioId()).orElse(null);
        assertEquals(portfolio, fromDao, "Retrieved portfolio should match");

        assertNull(portfolioDao.findById(999L).orElse(null), "Non-existent ID should return null");
    }

    @Test
    void testDelete() {
        Portfolio portfolio = new Portfolio(testUser, "Speculative", "Risky plays", new BigDecimal("1000.00"));
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio = portfolioDao.save(portfolio);

        assertNotNull(portfolioDao.findById(portfolio.getPortfolioId()).orElse(null), "Portfolio should exist");

        portfolioDao.deleteById(portfolio.getPortfolioId());

        assertNull(portfolioDao.findById(portfolio.getPortfolioId()).orElse(null), "Portfolio should be deleted");
        assertEquals(0, portfolioDao.findAll().size(), "No portfolios should remain");
    }

    @Test
    void testUpdate() {
        Portfolio portfolio = new Portfolio(testUser, "Long Term", "Retirement account", new BigDecimal("15000.00"));
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio = portfolioDao.save(portfolio);

        portfolio.setDescription("Updated description");
        portfolio.setTotal(new BigDecimal("16000.00"));
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio = portfolioDao.save(portfolio);

        Portfolio fromDao = portfolioDao.findById(portfolio.getPortfolioId()).orElse(null);
        assertNotNull(fromDao);
        assertEquals("Updated description", fromDao.getDescription());
        assertEquals(new BigDecimal("16000.00"), fromDao.getTotal());
    }

    @Test
    void testFindByUserId() {
        Portfolio p1 = new Portfolio(testUser, "User A - 1", "First portfolio", new BigDecimal("2000.00"));
        p1.setUpdatedAt(LocalDateTime.now());
        Portfolio p2 = new Portfolio(testUser, "User A - 2", "Second portfolio", new BigDecimal("3000.00"));
        p2.setUpdatedAt(LocalDateTime.now());

        portfolioDao.save(p1);
        portfolioDao.save(p2);

        List<Portfolio> userPortfolios = portfolioDao.findByUserId(testUser.getId());

        assertEquals(2, userPortfolios.size());
        assertTrue(userPortfolios.stream().anyMatch(p -> p.getName().equals("User A - 1")));
        assertTrue(userPortfolios.stream().anyMatch(p -> p.getName().equals("User A - 2")));
    }

    @Test
    void testGetPortfolioTotalValue() {
        // Save Stock
        Stock stock = new Stock();
        stock.setSymbol("MSFT");
        stock.setCompanyName("Microsoft");
        stock = stockDao.save(stock);

        // Save Portfolio
        Portfolio portfolio = new Portfolio(testUser, "ValueCalc", "Testing value query", new BigDecimal("0.00"));
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio = portfolioDao.save(portfolio);

        // Link with StockPortfolio
        StockPortfolio sp = new StockPortfolio();
        sp.setPortfolio(portfolio);
        sp.setStock(stock);
        sp.setQuantity(10);
        sp.setAvgBuyPrice(new BigDecimal("250.00"));
        stockPortfolioDao.save(sp);

        // Custom Query Test
        BigDecimal total = portfolioDao.getPortfolioTotalValue(portfolio.getPortfolioId());
        assertEquals(new BigDecimal("2500.00"), total);
    }
}