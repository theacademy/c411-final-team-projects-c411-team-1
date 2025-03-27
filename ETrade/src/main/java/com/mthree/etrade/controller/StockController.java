package com.mthree.etrade.controller;

import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.StockPrice;
import com.mthree.etrade.service.APILimitReachException;
import com.mthree.etrade.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>> allStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Stock> getStockBySymbol(@PathVariable("symbol") String symbol) {
        Stock stock = stockService.getStock(symbol);
        if(stock == null) {
            return new ResponseEntity<Stock>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Stock>(stock, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) {
        stockService.updateStock(stock);
        return new ResponseEntity<Stock>(stock, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addStock(@RequestBody Stock stock) {
        stockService.addStock(stock);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{symbol}")
    public ResponseEntity<Void> deleteStock(@PathVariable("symbol") String symbol) {
        stockService.deleteStock(symbol);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Stock>> searchByKeyword(@PathVariable("keyword") String keyword) {
        List<Stock> stocks = new ArrayList<>();

        //If API limit has been reached, let http client know with Service Unavailable status
        try {
            stocks = stockService.searchStock(keyword);
        } catch (APILimitReachException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(stocks);
        }

        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @GetMapping("/price/{symbol}")
    public ResponseEntity<BigDecimal> getPrice(@PathVariable("symbol") String symbol) {
        BigDecimal price;

        //If API limit has been reached, let http client know with Service Unavailable status
        try {
            price = stockService.getCurrentPrice(symbol);
        } catch (APILimitReachException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }

        if(price == null) {
            return new ResponseEntity<BigDecimal>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<BigDecimal>(price, HttpStatus.OK);
    }

    @GetMapping("history/{symbol}/{startDate}&{endDate}")
    public ResponseEntity<List<StockPrice>> getHistory(@PathVariable("symbol") String symbol, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) {
        LocalDate start;
        LocalDate end;
        List<StockPrice> priceList;

        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        //If API limit has been reached, let http client know with Service Unavailable status
        try {
            priceList = stockService.getStockHistory(symbol, start, end);
        } catch (APILimitReachException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }

        //if stock not found, return that as status to HTTP request
        if(priceList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(priceList);
        }

        return ResponseEntity.status(HttpStatus.OK).body(priceList);
    }

}
