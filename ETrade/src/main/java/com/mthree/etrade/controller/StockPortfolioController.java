package com.mthree.etrade.controller;

import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.StockPortfolioId;
import com.mthree.etrade.service.StockPortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> addStockToPortfolio(@RequestBody StockPortfolio stockPortfolio) {
        try {
            StockPortfolio created = stockPortfolioService.addStockToPortfolio(stockPortfolio);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add stock to portfolio: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateStock(@RequestBody StockPortfolio stockPortfolio) {
        try {
            StockPortfolioId id = new StockPortfolioId(
                    stockPortfolio.getPortfolio().getPortfolioId(),
                    stockPortfolio.getStock().getSymbol()
            );
            StockPortfolio updated = stockPortfolioService.updateStockInPortfolio(id, stockPortfolio);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update stock: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove/{portfolioId}/{symbol}")
    public ResponseEntity<?> removeStockFromPortfolio(@PathVariable Long portfolioId, @PathVariable String symbol) {
        try {
            StockPortfolioId id = new StockPortfolioId(portfolioId, symbol);
            stockPortfolioService.removeStockFromPortfolio(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to remove stock: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{portfolioId}/{symbol}")
    public ResponseEntity<?> getStockPortfolio(@PathVariable Long portfolioId, @PathVariable String symbol) {
        try {
            StockPortfolioId id = new StockPortfolioId(portfolioId, symbol);
            StockPortfolio result = stockPortfolioService.getStockPortfolioById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("StockPortfolio not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<StockPortfolio>> getStocksByPortfolioId(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(stockPortfolioService.getStocksByPortfolioId(portfolioId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StockPortfolio>> getPortfolioByUserId(@PathVariable Long userId) {
        List<StockPortfolio> stockPortfolios = stockPortfolioService.getPortfolioByUserId(userId);

        for (StockPortfolio stockPortfolio : stockPortfolios) {
            if (stockPortfolio.getPortfolio() != null) {
                stockPortfolio.getPortfolio().setUser(null);
                stockPortfolio.getPortfolio().setStockPortfolios(null);
                stockPortfolio.getPortfolio().setCreatedAt(null);
                stockPortfolio.getPortfolio().setUpdatedAt(null);
            }

            if (stockPortfolio.getStock() != null) {
                // Clear any unnecessary stock details if needed
                // stockPortfolio.getStock().setCompanyName(null);
            }
        }

        return ResponseEntity.ok(stockPortfolios);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockPortfolio>> getAllStockPortfolios() {
        return ResponseEntity.ok(stockPortfolioService.getAllStockPortfolios());
    }

    @GetMapping("/value/{userId}")
    public ResponseEntity<Double> getPortfolioValue(@PathVariable Long userId) {
        return ResponseEntity.ok(stockPortfolioService.calculatePortfolioValue(userId));
    }
}
