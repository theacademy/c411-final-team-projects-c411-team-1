package com.mthree.etrade.service;

import com.mthree.etrade.dao.StockDao;
import com.mthree.etrade.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class StockServiceImpl implements StockService{

    @Autowired
    StockDao stockDao;

    final String apiUri = "https://finnhub.io/api/v1/";
    final String apiKey = "cvhcdt9r01qrtb3noks0cvhcdt9r01qrtb3noksg";

    @Override
    public Stock getStock(String symbol) {
        return stockDao.findById(symbol).orElse(null);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockDao.findAll();
    }

    @Override
    public List<Stock> searchStockByNameExchange(String name, String exchange) {
        String searchStockUri = apiUri + "search?q=" + name + "&exchange=" +
                exchange + "&token=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        List<Stock> retrievedStocks = new ArrayList<>();

        //gets hashmap response from api and creates a list of that hashmap results
        HashMap<Object, Object> stockList = restTemplate.getForObject(searchStockUri, HashMap.class);
        List<HashMap<String, String>> stockInfo = (List<HashMap<String, String>>) stockList.get("result");

        //goes through list of resulting stock information and creates individual stock objects to return in list format
        for(Object stockObject : stockInfo) {
            HashMap<String, String> stockItem = (HashMap<String, String>) stockObject;
            Stock stock = new Stock();
            stock.setCompanyName(stockItem.get("description"));
            stock.setSymbol(stockItem.get("symbol"));
            retrievedStocks.add(stock);
        }

        return retrievedStocks;
    }

    @Override
    public List<Stock> searchStockByName(String name) {
        String searchStockUri = apiUri + "search?q=" + name +
                "&token=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        List<Stock> retrievedStocks = new ArrayList<>();

        //gets hashmap response from api and creates a list of that hashmap results
        HashMap<Object, Object> stockList = restTemplate.getForObject(searchStockUri, HashMap.class);
        List<HashMap<String, String>> stockInfo = (List<HashMap<String, String>>) stockList.get("result");

        //goes through list of resulting stock information and creates individual stock objects to return in list format
        for(Object stockObject : stockInfo) {
            HashMap<String, String> stockItem = (HashMap<String, String>) stockObject;
            Stock stock = new Stock();
            stock.setCompanyName(stockItem.get("description"));
            stock.setSymbol(stockItem.get("symbol"));
            retrievedStocks.add(stock);
        }

        return retrievedStocks;
    }

    @Override
    public BigDecimal getCurrentPrice(String symbol) {
        String getPriceUri = apiUri + "quote?symbol=" + symbol +
                "&token=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        BigDecimal price = null;

        //gets api json response and converts to a string
        String stockInfo = restTemplate.getForObject(getPriceUri, String.class);

        //parses the returned string to get the current price of the stock and turn it into BigDecimal object
        int costIndex = stockInfo.indexOf(":");
        int endCostIndex = stockInfo.indexOf(",");
        price = new BigDecimal(stockInfo.substring(costIndex+1, endCostIndex));

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
    public List<BigDecimal> getStockHistory(String symbol, Date startDate, Date endDate) {
        return List.of();
    }
}
