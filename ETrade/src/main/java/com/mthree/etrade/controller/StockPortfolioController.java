package com.mthree.etrade.controller;

import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.service.StockPortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/stock-portfolios")
@CrossOrigin
public class StockPortfolioController {

    @Autowired
    private StockPortfolioService stockPortfolioService;

    @GetMapping
    public ResponseEntity<List<StockPortfolio>> getAllStockPortfolios() {
        return ResponseEntity.ok(stockPortfolioService.getAllStockPortfolios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockPortfolio> getStockPortfolioById(@PathVariable Long id) {
        StockPortfolio sp = stockPortfolioService.getStockPortfolioById(id);
        return ResponseEntity.ok(sp);
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<StockPortfolio>> getByPortfolio(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(stockPortfolioService.getByPortfolioId(portfolioId));
    }

    @GetMapping("/portfolio/{portfolioId}/value")
    public ResponseEntity<BigDecimal> getTotalValueByPortfolio(@PathVariable Long portfolioId) {
        BigDecimal value = stockPortfolioService.calculatePortfolioValue(portfolioId);
        return ResponseEntity.ok(value);
    }

    @PostMapping
    public ResponseEntity<StockPortfolio> addStockToPortfolio(@RequestBody StockPortfolio stockPortfolio) {
        StockPortfolio created = stockPortfolioService.addStockToPortfolio(stockPortfolio);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockPortfolio> updateStockPortfolio(@PathVariable Long id, @RequestBody StockPortfolio updated) {
        StockPortfolio result = stockPortfolioService.updateStockPortfolio(id, updated);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockPortfolio(@PathVariable Long id) {
        stockPortfolioService.deleteStockPortfolio(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/portfolio/{portfolioId}/stock/{stockSymbol}")
    public ResponseEntity<Void> removeStockFromPortfolio(@PathVariable Long portfolioId, @PathVariable String stockSymbol) {
        stockPortfolioService.removeStockFromPortfolio(portfolioId, stockSymbol);
        return ResponseEntity.noContent().build();
    }
}
