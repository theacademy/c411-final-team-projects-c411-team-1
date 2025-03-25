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

}

