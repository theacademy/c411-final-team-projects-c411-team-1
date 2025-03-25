package com.mthree.etrade.controller;

import com.mthree.etrade.model.Stock;
import com.mthree.etrade.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/stock")
@CrossOrigin
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/stocks")
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

    @PutMapping("/update")
    public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) {
        stockService.updateStock(stock);
        return new ResponseEntity<Stock>(stock, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addStock(@RequestBody Stock stock) {
        stockService.addStock(stock);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<Stock>> searchByNameExchange(@PathVariable("name") String name) {
        List<Stock> stocks = stockService.searchStockByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @GetMapping("/search/{name}/{exchange}")
    public ResponseEntity<List<Stock>> searchByNameExchange(@PathVariable("name") String name, @PathVariable("exchange") String exchange) {
        List<Stock> stocks = stockService.searchStockByNameExchange(name, exchange);
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

}
