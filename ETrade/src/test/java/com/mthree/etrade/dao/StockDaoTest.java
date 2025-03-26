package com.mthree.etrade.dao;

import com.mthree.etrade.TestApplicationConfiguration;
import com.mthree.etrade.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class StockDaoTest {

    @Autowired
    StockDao stockDao;

    @BeforeEach
    void setUp() {
        List<Stock> stocks = stockDao.findAll();
        for(Stock s : stocks) {
            stockDao.deleteById(s.getSymbol());
        }
    }

    @Test
    void testAddFindAll() {
        Stock stock = new Stock();
        stock.setSymbol("aapl");
        stock.setCompanyName("Apple Inc");

        Stock stock2 = new Stock();
        stock2.setSymbol("ibm");
        stock2.setCompanyName("IBM Inc");

        stock = stockDao.save(stock);
        stock2 = stockDao.save(stock2);

        Stock stock3 = new Stock();
        stock3.setSymbol("aapl");
        stock3.setSymbol("Apple Inc");
        stock3 = stockDao.save(stock);

        List<Stock> retrievedStock = stockDao.findAll();

        assertEquals(2, retrievedStock.size(), "Should retrieve 2 stocks");
        assertTrue(retrievedStock.contains(stock));
        assertTrue(retrievedStock.contains(stock2));
    }

    @Test
    void testFindById() {
        Stock stock = new Stock();
        stock.setSymbol("aapl");
        stock.setCompanyName("Apple Inc");
        stock = stockDao.save(stock);

        Stock fromDao = stockDao.findById(stock.getSymbol()).orElse(null);

        assertEquals(stock, fromDao, "Retrieved stock should be equal");

        fromDao = stockDao.findById("test").orElse(null);
        assertNull(fromDao, "Retrieved stock should be null");
    }

    @Test
    void testDelete() {
        Stock stock = new Stock();
        stock.setSymbol("aapl");
        stock.setCompanyName("Apple Inc");
        stock = (Stock)stockDao.save(stock);

        assertEquals(stockDao.findById(stock.getSymbol()).orElse(null), stock, "Stock should be in memory.");

        stockDao.deleteById(stock.getSymbol());
        assertNull(stockDao.findById(stock.getSymbol()).orElse(null), "Retrieved stock should be null");

        List<Stock> stocks = stockDao.findAll();
        assertEquals(stocks.size(), 0, "List of stocks should be 0");
    }

    @Test
    void testUpdate() {
        Stock stock = new Stock();
        stock.setSymbol("aapl");
        stock.setCompanyName("Apple Inc");
        stock = (Stock) stockDao.save(stock);

        Stock stock2 = new Stock();
        stock2.setSymbol("aapl");
        stock2.setCompanyName("AppleInc");
        stock2 = (Stock) stockDao.save(stock2);

        List<Stock> stocks = stockDao.findAll();
        assertEquals(stocks.size(), 1, "Stock list should have 1");

        assertTrue(stocks.contains(stock2));
        assertFalse(stocks.contains(stock));

        Stock retrieved = stockDao.findById(stock.getSymbol()).orElse(null);
        assertTrue(retrieved.getCompanyName().equals("AppleInc"));
    }
}