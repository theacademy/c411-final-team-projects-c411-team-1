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
    private StockPortfolioService portfolioService;

    @PostMapping
    public StockPortfolio createPortfolio(@RequestBody StockPortfolioDTO dto) {
        return portfolioService.createPortfolio(dto);
    }

    @GetMapping
    public List<StockPortfolio> getAllPortfolios() {
        return portfolioService.getAllPortfolios();
    }

    @GetMapping("/{id}")
    public StockPortfolio getPortfolioById(@PathVariable Long id) {
        return portfolioService.getPortfolioById(id);
    }

    @PutMapping("/{id}")
    public StockPortfolio updatePortfolio(@PathVariable Long id, @RequestBody StockPortfolioDTO dto) {
        return portfolioService.updatePortfolio(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePortfolio(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
    }
}
}
