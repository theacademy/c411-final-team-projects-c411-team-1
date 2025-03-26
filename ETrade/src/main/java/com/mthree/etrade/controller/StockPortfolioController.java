package com.mthree.etrade.controller;

import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.service.StockPortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-portfolios")
@CrossOrigin
public class StockPortfolioController {

    @Autowired
    private StockPortfolioService stockPortfolioService;

    @PostMapping
    public ResponseEntity<StockPortfolio> addStockToPortfolio(@RequestBody StockPortfolio stockPortfolio) {
        StockPortfolio created = stockPortfolioService.addStockToPortfolio(stockPortfolio);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockPortfolio> updateStockInPortfolio(@PathVariable Long id, @RequestBody StockPortfolio stockPortfolio) {
        StockPortfolio updated = stockPortfolioService.updateStockInPortfolio(id, stockPortfolio);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStockFromPortfolio(@PathVariable Long id) {
        stockPortfolioService.removeStockFromPortfolio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockPortfolio> getStockPortfolioById(@PathVariable Long id) {
        return ResponseEntity.ok(stockPortfolioService.getStockPortfolioById(id));
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<StockPortfolio>> getStocksByPortfolioId(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(stockPortfolioService.getStocksByPortfolioId(portfolioId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StockPortfolio>> getPortfolioByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(stockPortfolioService.getPortfolioByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<StockPortfolio>> getAllStockPortfolios() {
        return ResponseEntity.ok(stockPortfolioService.getAllStockPortfolios());
    }

    @GetMapping("/value/user/{userId}")
    public ResponseEntity<Double> calculatePortfolioValue(@PathVariable Long userId) {
        return ResponseEntity.ok(stockPortfolioService.calculatePortfolioValue(userId));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByPortfolioAndStock(
            @RequestParam Long portfolioId,
            @RequestParam String stockSymbol) {
        return ResponseEntity.ok(stockPortfolioService.existsByPortfolioAndStock(portfolioId, stockSymbol));
    }
}
