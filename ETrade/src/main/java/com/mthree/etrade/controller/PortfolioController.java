package com.mthree.etrade.controller;

import com.mthree.etrade.model.Portfolio;
import com.mthree.etrade.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable Long id) {
        Portfolio portfolio = portfolioService.getPortfolioById(id);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping
    public ResponseEntity<List<Portfolio>> getAllPortfolios() {
        return ResponseEntity.ok(portfolioService.getAllPortfolios());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Portfolio>> getPortfoliosByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(portfolioService.getPortfoliosByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        Portfolio created = portfolioService.savePortfolio(portfolio);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable Long id, @RequestBody Portfolio updatedPortfolio) {
        Portfolio existing = portfolioService.getPortfolioById(id);
        existing.setName(updatedPortfolio.getName());
        existing.setDescription(updatedPortfolio.getDescription());
        existing.setTotal(updatedPortfolio.getTotal());
        existing.setUpdatedAt(updatedPortfolio.getUpdatedAt());
        return ResponseEntity.ok(portfolioService.savePortfolio(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        portfolioService.deletePortfolioById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<BigDecimal> getPortfolioTotalValue(@PathVariable Long id) {
        BigDecimal total = portfolioService.calculateTotalValue(id);
        return ResponseEntity.ok(total);
    }

    // Performance Metrics
}
