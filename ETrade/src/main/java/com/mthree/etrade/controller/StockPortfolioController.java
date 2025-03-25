package com.mthree.etrade.controller;

import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.service.StockPortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class StockPortfolioController {

    @Autowired
    private StockPortfolioService service;

    @GetMapping("/{id}")
    public StockPortfolio getPortfolio(@PathVariable int id) {
        return service.getPortfolio(id);
    }

    @PostMapping
    public StockPortfolio createPortfolio(@RequestBody StockPortfolio portfolio) {
        return service.createPortfolio(portfolio);
    }

    @PutMapping("/{id}")
    public void updatePortfolio(@PathVariable int id, @RequestBody StockPortfolio portfolio) {
        portfolio.setPortfolioId(id);
        service.updatePortfolio(portfolio);
    }

    @DeleteMapping("/{id}")
    public void deletePortfolio(@PathVariable int id) {
        service.deletePortfolio(id);
    }
}
