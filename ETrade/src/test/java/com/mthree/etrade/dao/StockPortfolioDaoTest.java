package com.mthree.etrade.dao;

import com.mthree.etrade.TestApplicationConfiguration;
import com.mthree.etrade.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class StockPortfolioDaoTest {

    @Autowired
    private StockPortfolioDao stockPortfolioDao;

    @Autowired
    private PortfolioDao portfolioDAO;

    @Autowired
    private UserDao userDAO;

    @Autowired
    private StockDao stockDAO;

    private Portfolio portfolio;
    private Stock stock;
    private StockPortfolio stockPortfolio;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setBalance(new BigDecimal("500"));
        user = userDAO.save(user);

        portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setName("Tech Portfolio");
        portfolio.setDescription("Tech investments");
        portfolio.setTotal(BigDecimal.ZERO);
        portfolio.setCreatedAt(LocalDateTime.now());
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolio = portfolioDAO.save(portfolio);

        stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setCompanyName("Apple Inc.");
        stock = stockDAO.save(stock);

        stockPortfolio = new StockPortfolio();
        stockPortfolio.setPortfolio(portfolio);
        stockPortfolio.setStock(stock);
        stockPortfolio.setQuantity(10);
        stockPortfolio.setAvgBuyPrice(new BigDecimal("150.00"));
        stockPortfolio.setLastUpdated(LocalDateTime.now());
        stockPortfolioDao.save(stockPortfolio);
    }

    @AfterEach
    void tearDown() {
        stockPortfolioDao.deleteByPortfolioIdAndStockSymbol(portfolio.getPortfolioId(), stock.getSymbol());
        stockDAO.deleteById(stock.getSymbol());
        portfolioDAO.deleteById(portfolio.getPortfolioId());
        userDAO.deleteById(user.getId());
    }

    @Test
    void testFindByPortfolioId() {
        List<StockPortfolio> result = stockPortfolioDao.findByPortfolioId(portfolio.getPortfolioId());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testFindByUserId() {
        List<StockPortfolio> result = stockPortfolioDao.findByPortfolioUserId(portfolio.getUser().getId());
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCompositeId() {
        StockPortfolioId id = new StockPortfolioId(portfolio.getPortfolioId(), stock.getSymbol());
        Optional<StockPortfolio> result = stockPortfolioDao.findById(id);
        assertTrue(result.isPresent());
        assertEquals(stock.getSymbol(), result.get().getStock().getSymbol());
    }

    @Test
    void testDeleteByCompositeId() {
        stockPortfolioDao.deleteByPortfolioIdAndStockSymbol(portfolio.getPortfolioId(), stock.getSymbol());
        List<StockPortfolio> result = stockPortfolioDao.findByPortfolioId(portfolio.getPortfolioId());
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByStockSymbol() {
        List<StockPortfolio> result = stockPortfolioDao.findByStockSymbol(stock.getSymbol());
        assertFalse(result.isEmpty());
    }


    }

