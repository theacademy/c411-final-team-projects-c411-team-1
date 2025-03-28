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
    final String apiKey = "3AVUK1YNQ9OE5QMD";
//    final String backupApiKey = "LWEUUQLHSGC08HJJ";
//    final String backupApiKey2 ="UEAUBTP7WIJ9RKPE";

    @Override
    public Stock getStock(String symbol) {
        // First check the database
        Stock stock = stockDao.findById(symbol).orElse(null);

        // If not in database, try to get it from the API
        if (stock == null) {
            try {
                // Use the search API to get stock details
                String searchStockUri = apiUri + "SYMBOL_SEARCH&keywords=" + symbol +
                        "&apikey=" + apiKey;
                RestTemplate restTemplate = new RestTemplate();

                HashMap<String, Object> response = restTemplate.getForObject(searchStockUri, HashMap.class);

                // Check for API limit
                if (response.get("Information") != null) {
                    throw new APILimitReachException("API limit reached");
                }

                // Get best matches
                List<HashMap<String, String>> matches = (List<HashMap<String, String>>) response.get("bestMatches");

                // Find an exact match for the symbol
                if (matches != null && !matches.isEmpty()) {
                    for (HashMap<String, String> match : matches) {
                        if (match.get("1. symbol").equalsIgnoreCase(symbol)) {
                            // Create and save the stock
                            stock = new Stock();
                            stock.setSymbol(symbol);
                            stock.setCompanyName(match.get("2. name"));
                            stockDao.save(stock);
                            break;
                        }
                    }
                }
            } catch (APILimitReachException e) {
                throw e;
            } catch (Exception e) {
                // Log the error but don't throw it
                e.printStackTrace();
            }
        }

        return stock;
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockDao.findAll();
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
        //check if stock exists before trying to delete
        Stock stock = stockDao.findById(symbol).orElse(null);
        if(stock == null) {
            return;
        }

        stockDao.deleteById(symbol);
    }

    @Override
    public List<Stock> searchStock(String keyword) {
        String searchStockUri = apiUri + "SYMBOL_SEARCH&keywords=" + keyword +
                "&apikey=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        List<Stock> retrievedStocks = new ArrayList<>();

        //gets hashmap response from api and creates a list of that hashmap results
        HashMap<String, Object> bestMatches = restTemplate.getForObject(searchStockUri, HashMap.class);
        List<HashMap<String, String>> stockList = (List<HashMap<String, String>>) bestMatches.get("bestMatches");

        //tests if api limit for key has been reached
        if(bestMatches.get("Information") != null) {
            throw new APILimitReachException("API limit of 25 calls per day has been reached.");
        }

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
        String getPriceUri = apiUri + "GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
        System.out.println("Making API call to: " + getPriceUri);
        RestTemplate restTemplate = new RestTemplate();
        BigDecimal price = null;

        //gets api json response and converts to a Hashmap of strings
        HashMap<String, HashMap<String, String>> stockInfo = restTemplate.getForObject(getPriceUri, HashMap.class);
        HashMap<String, String> globalQuote = stockInfo.get("Global Quote");

        //tests if api limit for key has been reached
        if(stockInfo.get("Information") != null) {
            throw new APILimitReachException("API limit of 25 calls per day has been reached.");
        }

        try {
            //gets price from returned hashmap and stores in price variable
            price = new BigDecimal(globalQuote.get("05. price"));
        } catch (Exception ex) {    //if not able to parse or nothing in global quote then return null for price
            return null;
        }

        return price;
    }

    @Override
    public List<StockPrice> getStockHistory(String symbol, LocalDate startDate, LocalDate endDate) {
        String stockHistoryUri = apiUri + "TIME_SERIES_DAILY&symbol=" + symbol +
                "&apikey=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        List<StockPrice> stockHistory = new ArrayList<>();

        HashMap<String, Object> data = restTemplate.getForObject(stockHistoryUri, HashMap.class);
        HashMap<String, HashMap<String, String>> timeSeries = (HashMap<String, HashMap<String, String>>) data.get("Time Series (Daily)");

        //tests if api limit for key has been reached
        if(data.get("Information") != null) {
            throw new APILimitReachException("API limit of 25 calls per day has been reached.");
        }

        //if stock cant be found, return null to indicate to controller
        if(data.get("Error Message") != null) {
            return null;
        }

        Set<String> dates = timeSeries.keySet();

        //goes through the data of dates retrieved from the api call and filters them out based on date, adds not filtered ones to stockHistory list
        for(String d : dates) {
            LocalDate currentDate = LocalDate.parse(d);
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