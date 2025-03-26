package com.mthree.etrade.controller;

import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.StockPrice;
import com.mthree.etrade.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        return new ResponseEntity<Stock>(stock, HttpStatus.OK);
    }

    @GetMapping("/price/{symbol}")
    public ResponseEntity<BigDecimal> getPrice(@PathVariable("symbol") String symbol) {
        BigDecimal price = stockService.getCurrentPrice(symbol);
        return new ResponseEntity<BigDecimal>(price, HttpStatus.OK);
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
        List<Stock> stocks = stockService.searchStock(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @GetMapping("history/{symbol}/{startDate}&{endDate}")
    public ResponseEntity<List<StockPrice>> getHistory(@PathVariable("symbol") String symbol, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<StockPrice> priceList = stockService.getStockHistory(symbol, start, end);
        return ResponseEntity.status(HttpStatus.OK).body(priceList);
    }

}
