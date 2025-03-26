package com.mthree.etrade.service;

import com.mthree.etrade.dao.StockDao;
import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.StockPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class StockServiceImpl implements StockService{

    @Autowired
    private StockDao stockDao;

    final String apiUri = "https://www.alphavantage.co/query?function=";
    final String apiKey = "X6N1ESY3V8OZSDJN";

    @Override
    public Stock getStock(String symbol) {
        return stockDao.findById(symbol).orElse(null);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockDao.findAll();
    }

    @Override
    public List<Stock> searchStock(String keyword) {
        String searchStockUri = apiUri + "SYMBOL_SEARCH&keywords=" + keyword +
                "&apikey=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        List<Stock> retrievedStocks = new ArrayList<>();

        //gets hashmap response from api and creates a list of that hashmap results
        HashMap<Object, Object> bestMatches = restTemplate.getForObject(searchStockUri, HashMap.class);
        List<HashMap<String, String>> stockList = (List<HashMap<String, String>>) bestMatches.get("bestMatches");

        //goes through list of resulting stock information and creates individual stock objects to return in list format
        for(Object stockObject : stockList) {
            HashMap<String, String> stockItem = (HashMap<String, String>) stockObject;
            Stock stock = new Stock();
            stock.setCompanyName(stockItem.get("2. name"));
            stock.setSymbol(stockItem.get("1. symbol"));
            retrievedStocks.add(stock);
        }

        return retrievedStocks;
    }

    @Override
    public BigDecimal getCurrentPrice(String symbol) {
        String getPriceUri = apiUri + "GLOBAL_QUOTE&symbol=" + symbol +
                "&apikey=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        BigDecimal price = null;

        //gets api json response and converts to a Hashmap of strings
        HashMap<String, HashMap<String, String>> stockInfo = restTemplate.getForObject(getPriceUri, HashMap.class);
        HashMap<String, String> globalQuote = stockInfo.get("Global Quote");

        //gets price from returned hashmap and stores in price variable
        price = new BigDecimal(globalQuote.get("05. price"));

        return price;
    }

    @Override
    public void updateStock(Stock stock) {
        stockDao.save(stock);
    }

    @Override
    public void addStock(Stock stock) {
        stockDao.save(stock);
    }

    @Override
    public void deleteStock(String symbol) {
        stockDao.deleteById(symbol);
    }

    @Override
    public List<StockPrice> getStockHistory(String symbol, LocalDate startDate, LocalDate endDate) {
        String stockHistoryUri = apiUri + "TIME_SERIES_DAILY&symbol=" + symbol +
                "&apikey=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        List<StockPrice> stockHistory = new ArrayList<>();

        HashMap<String, Object> data = restTemplate.getForObject(stockHistoryUri, HashMap.class);
        HashMap<String, HashMap<String, String>> timeSeries = (HashMap<String, HashMap<String, String>>) data.get("Time Series (Daily)");

        Set<String> dates = timeSeries.keySet();

        for(String d : dates) {
            LocalDate currentDate = LocalDate.parse(d);
            // FIXED: Updated the comparison to correctly include both startDate and endDate
            // Using compareTo() >= 0 and <= 0 instead of isAfter/isBefore which are exclusive
            if(currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0) {
                HashMap<String, String> stockDate = timeSeries.get(d);
                StockPrice stockPrice = new StockPrice();
                stockPrice.setDate(currentDate);
                stockPrice.setOpen(new BigDecimal(stockDate.get("1. open")));
                stockPrice.setHigh(new BigDecimal(stockDate.get("2. high")));
                stockPrice.setLow(new BigDecimal(stockDate.get("3. low")));
                stockPrice.setClose(new BigDecimal(stockDate.get("4. close")));
                stockPrice.setVolume(Long.parseLong(stockDate.get("5. volume")));
                stockHistory.add(stockPrice);
            }
        }

        return stockHistory;
    }
}