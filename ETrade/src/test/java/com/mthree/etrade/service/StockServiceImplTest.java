package com.mthree.etrade.service;

import com.mthree.etrade.TestApplicationConfiguration;
import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.StockPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class StockServiceImplTest {

    @Autowired
    StockService stockService;

    @BeforeEach
    void setUp() {
        List<Stock> stocks = stockService.getAllStocks();
        for(Stock s : stocks) {
            stockService.deleteStock(s.getSymbol());
        }
    }

    @Test
    void testGetStock() {
        Stock stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setCompanyName("Apple Inc");
        stockService.addStock(stock);

        Stock fromService = stockService.getStock("AAPL");

        assertNotNull(fromService);
        assertEquals(stock, fromService, "Retrieved stock should equal Apple stock");
        assertTrue(fromService.getCompanyName().equals("Apple Inc"), "Name should be Apple Inc");
        assertTrue(fromService.getSymbol().equals("AAPL"), "Symbol should be AAPL");

        //test a lot of potential inputs from controller that user puts in
        fromService = stockService.getStock("tesla");
        assertNull(fromService, "Retrieved stock should be null");
        fromService = stockService.getStock("t20sla");
        assertNull(fromService, "Retrieved stock should be null");
        fromService = stockService.getStock("");
        assertNull(fromService, "Retrieved stock should be null");
    }

    @Test
    void testAddGetAllStocks() {
        Stock stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setCompanyName("Apple Inc");
        stockService.addStock(stock);

        Stock stock2 = new Stock();
        stock2.setSymbol("IBM");
        stock2.setCompanyName("IBM Inc");
        stockService.addStock(stock2);

        List<Stock> stocks = stockService.getAllStocks();

        assertEquals(2, stocks.size(), "List should have 2 stocks");
        assertTrue(stocks.contains(stock));
        assertTrue(stocks.contains(stock2));

    }

    @Test
    void testUpdateStock() {
        Stock stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setCompanyName("Apple Inc");
        stockService.addStock(stock);

        Stock fromService = stockService.getStock(stock.getSymbol());

        assertEquals(stock, fromService, "Stocks should be equal");

        stock.setCompanyName("Tesla");
        stockService.updateStock(stock);

        Stock updated = stockService.getStock(stock.getSymbol());
        assertNotEquals(fromService, updated, "Updated stock should not equal the original stock");
        assertTrue(updated.getCompanyName().equals("Tesla"), "Updated stock should have different company name of 'Tesla'");

        List<Stock> stocks = stockService.getAllStocks();
        assertEquals(1, stocks.size(), "List should only have one stock");
    }

    @Test
    void testDeleteStock() {
        Stock stock = new Stock();
        stock.setSymbol("AAPL");
        stock.setCompanyName("Apple Inc");
        stockService.addStock(stock);

        List<Stock> stocks = stockService.getAllStocks();
        assertEquals(1, stocks.size(), "List should have one stock");

        stockService.deleteStock("test");
        stocks = stockService.getAllStocks();
        assertEquals(1, stocks.size(), "List should still only have one stock");

        stockService.deleteStock("AAPL");
        stocks = stockService.getAllStocks();
        assertEquals(0, stocks.size(), "List should be empty");
        assertNull(stockService.getStock("AAPL"), "Apple stock should not exist anymore");

    }

//    //Run these tests when api limit has not been reached
//    @Test
//    void testSearchStock() {
//        String correctKeyword = "Apple";
//        String fakeKeyword = "Asdjf sldjf";
//
//        Stock stock = new Stock();
//        stock.setSymbol("AAPL");
//        stock.setCompanyName("Apple Inc");
//
//        List<Stock> results = stockService.searchStock(correctKeyword);
//        assertNotNull(results, "Results should have returned something");
//        assertEquals(10, results.size(), "Results should return 10 best matches");
//        assertTrue(results.contains(stock), "Results should have the Apple stock");
//
//        results = stockService.searchStock(fakeKeyword);
//        assertEquals(0, results.size(), "Results will be empty for the fake keyword");
//    }
//
//    @Test
//    void testGetCurrentPrice() {
//        BigDecimal price = stockService.getCurrentPrice("AAPL");
//        assertNotNull(price, "Price should not be null");
//
//        price = stockService.getCurrentPrice("as dkaj");
//        assertNull(price, "Price should be null, as that stock does not exist");
//    }
//
//    @Test
//    void testGetStockHistory() {
//        List<StockPrice> stockPrices = stockService.getStockHistory("AAPL", LocalDate.parse("2025-03-03"), LocalDate.parse("2025-03-07"));
//        assertNotNull(stockPrices, "Stock price history list should not be null");
//        assertEquals(5, stockPrices.size(), "List should contain 5 stock prices");
//
//        stockPrices = stockService.getStockHistory("sada", LocalDate.parse("2025-03-03"), LocalDate.parse("2025-03-07"));
//        assertNull(stockPrices, "Stock history should be null");
//
//        stockPrices = stockService.getStockHistory("AAPL", LocalDate.now().plusDays(1), LocalDate.now());
//        assertEquals(0, stockPrices.size(), "Stock history should be empty");
//    }



    /************************************************************************************************
                            Run these test when api limit is reached
     ************************************************************************************************/

    @Test
    void testSearchStockAPILimitReached() {
        String keyword = "Apple";

        assertThrows(
                APILimitReachException.class,
                () -> stockService.searchStock(keyword),
                "Expect search stock to throw API limit exception but didn't"
        );
    }

    @Test
    void testGetCurrentPriceAPILimitReached() {
        assertThrows(
                APILimitReachException.class,
                () -> stockService.getCurrentPrice("AAPL"),
                "Expect get Price to throw API limit exception but didn't"
        );
    }

    @Test
    void testGetStockHistoryAPILimitReached() {
        assertThrows(
                APILimitReachException.class,
                () -> stockService.getStockHistory("AAPL", LocalDate.parse("2025-03-03"), LocalDate.parse("2025-03-07")),
                "Expect search stock to throw API limit exception but didn't"
        );
    }
}