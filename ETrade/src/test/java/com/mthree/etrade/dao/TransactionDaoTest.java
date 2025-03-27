package com.mthree.etrade.dao;

import com.mthree.etrade.TestApplicationConfiguration;
import com.mthree.etrade.model.Portfolio;
import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.Transaction;
import com.mthree.etrade.model.User;
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
public class TransactionDaoTest {

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private PortfolioDao portfolioDao;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private UserDao userDao;

    /**
     * Test all DAO operations in a single test.
     * This approach avoids issues with state between tests.
     */
    @Test
    public void testTransactionDao() {
        // Create test entities to work with
        User user = createTestUser();
        Portfolio portfolio = createTestPortfolio(user);
        Stock stock = createTestStock();
        Transaction transaction = createTestTransaction(portfolio, stock);

        // Test findById
        Transaction retrievedTransaction = transactionDao.findById(transaction.getId()).orElse(null);
        assertNotNull(retrievedTransaction);
        assertEquals(transaction.getId(), retrievedTransaction.getId());
        assertEquals(transaction.getQuantity(), retrievedTransaction.getQuantity());
        assertEquals(0, transaction.getPrice().compareTo(retrievedTransaction.getPrice()));

        // Test findAll
        List<Transaction> allTransactions = transactionDao.findAll();
        boolean containsTestTransaction = false;
        for (Transaction t : allTransactions) {
            if (t.getId().equals(transaction.getId())) {
                containsTestTransaction = true;
                break;
            }
        }
        assertTrue(containsTestTransaction);

        // Test findByPortfolioPortfolioId
        List<Transaction> portfolioTransactions = transactionDao.findByPortfolioPortfolioId(portfolio.getPortfolioId());
        assertEquals(1, portfolioTransactions.size());
        assertEquals(transaction.getId(), portfolioTransactions.get(0).getId());

        // Test findByUserId
        List<Transaction> userTransactions = transactionDao.findByUserId(user.getId());
        assertEquals(1, userTransactions.size());
        assertEquals(transaction.getId(), userTransactions.get(0).getId());

        // Test findByStockSymbol
        List<Transaction> stockTransactions = transactionDao.findByStockSymbol(stock.getSymbol());
        assertEquals(1, stockTransactions.size());
        assertEquals(transaction.getId(), stockTransactions.get(0).getId());

        // Test findByTransactionType
        List<Transaction> buyTransactions = transactionDao.findByTransactionType("BUY");
        assertEquals(1, buyTransactions.size());
        assertEquals(transaction.getId(), buyTransactions.get(0).getId());

        List<Transaction> sellTransactions = transactionDao.findByTransactionType("SELL");
        assertEquals(0, sellTransactions.size());

        // Create a sell transaction
        Transaction sellTransaction = new Transaction();
        sellTransaction.setPortfolio(portfolio);
        sellTransaction.setStock(stock);
        sellTransaction.setQuantity(5);
        sellTransaction.setPrice(new BigDecimal("160.00"));
        sellTransaction.setDate(LocalDateTime.now());
        sellTransaction.setTransactionType("SELL");
        transactionDao.save(sellTransaction);

        // Test SELL transaction type
        sellTransactions = transactionDao.findByTransactionType("SELL");
        assertEquals(1, sellTransactions.size());
        assertEquals(sellTransaction.getId(), sellTransactions.get(0).getId());

        // Test update
        transaction.setQuantity(20);
        transaction.setPrice(new BigDecimal("155.00"));
        transactionDao.save(transaction);

        Transaction updatedTransaction = transactionDao.findById(transaction.getId()).orElse(null);
        assertNotNull(updatedTransaction);
        assertEquals(20, updatedTransaction.getQuantity());
        assertEquals(0, new BigDecimal("155.00").compareTo(updatedTransaction.getPrice()));

        // Test findByDateBetween
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        List<Transaction> dateRangeTransactions = transactionDao.findByDateBetween(startDate, endDate);
        assertTrue(dateRangeTransactions.size() >= 2); // Both our transactions should be there

        // Test findByPortfolioIdAndStockSymbol
        List<Transaction> portfolioStockTransactions =
                transactionDao.findByPortfolioPortfolioIdAndStockSymbol(portfolio.getPortfolioId(), stock.getSymbol());
        assertEquals(2, portfolioStockTransactions.size());

        // Test delete
        transactionDao.delete(sellTransaction);
        assertNull(transactionDao.findById(sellTransaction.getId()).orElse(null));

        // Clean up before completing test
        transactionDao.delete(transaction);
        stockDao.delete(stock);
        portfolioDao.delete(portfolio);
        userDao.delete(user);
    }

    private User createTestUser() {
        User user = new User();
        user.setName("TransactionTest User");
        user.setEmail("transactiontest@example.com");
        user.setPassword("password");
        user.setBalance(new BigDecimal("10000.00"));
        return userDao.save(user);
    }

    private Portfolio createTestPortfolio(User user) {
        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setName("Transaction Test Portfolio");
        portfolio.setDescription("For transaction testing");
        portfolio.setTotal(new BigDecimal("5000.00"));
        portfolio.setCreatedAt(LocalDateTime.now());
        portfolio.setUpdatedAt(LocalDateTime.now());
        return portfolioDao.save(portfolio);
    }

    private Stock createTestStock() {
        Stock stock = new Stock();
        stock.setSymbol("TRX");  // Unique symbol for transaction tests
        stock.setCompanyName("Transaction Test Company");
        return stockDao.save(stock);
    }

    private Transaction createTestTransaction(Portfolio portfolio, Stock stock) {
        Transaction transaction = new Transaction();
        transaction.setPortfolio(portfolio);
        transaction.setStock(stock);
        transaction.setQuantity(10);
        transaction.setPrice(new BigDecimal("150.00"));
        transaction.setDate(LocalDateTime.now());
        transaction.setTransactionType("BUY");
        return transactionDao.save(transaction);
    }
}