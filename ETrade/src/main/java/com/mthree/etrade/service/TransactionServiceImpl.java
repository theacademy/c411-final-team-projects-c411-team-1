package com.mthree.etrade.service;

import com.mthree.etrade.dao.StockDao;
import com.mthree.etrade.dao.PortfolioDao;
import com.mthree.etrade.dao.StockPortfolioDao;
import com.mthree.etrade.dao.TransactionDao;
import com.mthree.etrade.model.Portfolio;
import com.mthree.etrade.model.Stock;
import com.mthree.etrade.model.StockPortfolio;
import com.mthree.etrade.model.Transaction;
import com.mthree.etrade.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDAO;
    private final PortfolioDao portfolioDAO;
    private final StockDao stockDAO;
    private final StockPortfolioDao stockPortfolioDAO;
    private final StockService stockService;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDAO,
                                  PortfolioDao portfolioDAO,
                                  StockDao stockDAO,
                                  StockPortfolioDao stockPortfolioDAO,
                                  StockService stockService) {
        this.transactionDAO = transactionDAO;
        this.portfolioDAO = portfolioDAO;
        this.stockDAO = stockDAO;
        this.stockPortfolioDAO = stockPortfolioDAO;
        this.stockService = stockService;
    }

    @Override
    public Transaction findById(Long id) {
        return transactionDAO.findById(id)
                .orElse(null);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionDAO.findAll();
    }

    @Override
    public List<Transaction> findByPortfolioId(Long portfolioId) {
        return transactionDAO.findByPortfolioPortfolioId(portfolioId);
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return transactionDAO.findByUserId(userId);
    }

    @Override
    public List<Transaction> findByStockSymbol(String stockSymbol) {
        return transactionDAO.findByStockSymbol(stockSymbol);
    }

    @Override
    public List<Transaction> findByTransactionType(String type) {
        return transactionDAO.findByTransactionType(type);
    }

    @Override
    public List<Transaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionDAO.findByDateBetween(startDate, endDate);
    }

    @Override
    @Transactional
    public Transaction executeBuyTransaction(Long portfolioId, String stockSymbol, int quantity, BigDecimal price) {
        // Validate the transaction
        if (!validateTransaction(portfolioId, stockSymbol, quantity, price, "BUY")) {
            throw new IllegalArgumentException("Invalid transaction parameters");
        }

        // Get portfolio and stock
        Optional<Portfolio> optionalPortfolio = portfolioDAO.findById(portfolioId);
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);

        if (optionalPortfolio.isEmpty() || optionalStock.isEmpty()) {
            throw new IllegalArgumentException("Portfolio or Stock not found");
        }

        Portfolio portfolio = optionalPortfolio.get();
        Stock stock = optionalStock.get();

        // Calculate total cost
        BigDecimal totalCost = price.multiply(new BigDecimal(quantity));

        // Check if user has enough balance
        User user = portfolio.getUser();
        if (user.getBalance().compareTo(totalCost) < 0) {
            throw new IllegalStateException("Insufficient balance to execute buy transaction");
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setPortfolio(portfolio);
        transaction.setStock(stock);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setDate(LocalDateTime.now());
        transaction.setTransactionType("BUY");

        // Update user balance
        user.setBalance(user.getBalance().subtract(totalCost));

        // Update stock portfolio
        updateStockPortfolioForBuy(portfolio, stock, quantity, price);

        // Update portfolio total value
        updatePortfolioTotalValue(portfolio);

        // Save and return the transaction
        return transactionDAO.save(transaction);
    }

    @Override
    @Transactional
    public Transaction executeSellTransaction(Long portfolioId, String stockSymbol, int quantity, BigDecimal price) {
        // Validate the transaction
        if (!validateTransaction(portfolioId, stockSymbol, quantity, price, "SELL")) {
            throw new IllegalArgumentException("Invalid transaction parameters");
        }

        // Get portfolio and stock
        Optional<Portfolio> optionalPortfolio = portfolioDAO.findById(portfolioId);
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);

        if (optionalPortfolio.isEmpty() || optionalStock.isEmpty()) {
            throw new IllegalArgumentException("Portfolio or Stock not found");
        }

        Portfolio portfolio = optionalPortfolio.get();
        Stock stock = optionalStock.get();

        // Check if user has enough stock to sell
        Optional<StockPortfolio> optionalStockPortfolio = stockPortfolioDAO.findByPortfolio_PortfolioIdAndStock_Symbol(portfolioId, stockSymbol);
        if (optionalStockPortfolio.isEmpty() || optionalStockPortfolio.get().getQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock quantity to execute sell transaction");
        }

        // Calculate total amount
        BigDecimal totalAmount = price.multiply(new BigDecimal(quantity));

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setPortfolio(portfolio);
        transaction.setStock(stock);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setDate(LocalDateTime.now());
        transaction.setTransactionType("SELL");

        // Update user balance
        User user = portfolio.getUser();
        user.setBalance(user.getBalance().add(totalAmount));

        // Update stock portfolio
        updateStockPortfolioForSell(portfolio, stock, quantity);

        // Update portfolio total value
        updatePortfolioTotalValue(portfolio);

        // Save and return the transaction
        return transactionDAO.save(transaction);
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionDAO.save(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        transactionDAO.deleteById(id);
    }

    @Override
    public boolean validateTransaction(Long portfolioId, String stockSymbol, int quantity, BigDecimal price, String transactionType) {
        // Basic validation
        if (portfolioId <= 0 || stockSymbol == null || stockSymbol.isEmpty() ||
                quantity <= 0 || price == null || price.compareTo(BigDecimal.ZERO) <= 0 ||
                transactionType == null || (!transactionType.equals("BUY") && !transactionType.equals("SELL"))) {
            return false;
        }

        // Check if portfolio and stock exist
        Optional<Portfolio> optionalPortfolio = portfolioDAO.findById(portfolioId);
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);

        if (optionalPortfolio.isEmpty() || optionalStock.isEmpty()) {
            return false;
        }

        // Transaction type specific validation
        if (transactionType.equals("BUY")) {
            // Check if user has enough balance
            Portfolio portfolio = optionalPortfolio.get();
            User user = portfolio.getUser();
            BigDecimal totalCost = price.multiply(new BigDecimal(quantity));
            return user.getBalance().compareTo(totalCost) >= 0;
        } else if (transactionType.equals("SELL")) {
            // Check if user has enough stock to sell
            Optional<StockPortfolio> optionalStockPortfolio = stockPortfolioDAO.findByPortfolio_PortfolioIdAndStock_Symbol(portfolioId, stockSymbol);
            return optionalStockPortfolio.isPresent() && optionalStockPortfolio.get().getQuantity() >= quantity;
        }

        return false;
    }

    /**
     * Helper method to update stock portfolio for a buy transaction
     */
    private void updateStockPortfolioForBuy(Portfolio portfolio, Stock stock, int quantity, BigDecimal price) {
        Optional<StockPortfolio> optionalStockPortfolio = stockPortfolioDAO.findByPortfolio_PortfolioIdAndStock_Symbol(portfolio.getPortfolioId(), stock.getSymbol());

        if (optionalStockPortfolio.isPresent()) {
            // Update existing stock portfolio
            StockPortfolio stockPortfolio = optionalStockPortfolio.get();
            int newQuantity = stockPortfolio.getQuantity() + quantity;

            // Calculate new average buy price
            BigDecimal currentValue = stockPortfolio.getAvgBuyPrice()
                    .multiply(new BigDecimal(stockPortfolio.getQuantity()));
            BigDecimal newValue = price.multiply(new BigDecimal(quantity));
            BigDecimal totalValue = currentValue.add(newValue);
            BigDecimal newAverage = totalValue.divide(new BigDecimal(newQuantity), 2, BigDecimal.ROUND_HALF_UP);

            stockPortfolio.setQuantity(newQuantity);
            stockPortfolio.setAvgBuyPrice(newAverage);
            stockPortfolio.setLastUpdated(LocalDateTime.now());
            stockPortfolioDAO.save(stockPortfolio);
        } else {
            // Create new stock portfolio entry
            StockPortfolio stockPortfolio = new StockPortfolio();
            stockPortfolio.setPortfolio(portfolio);
            stockPortfolio.setStock(stock);
            stockPortfolio.setQuantity(quantity);
            stockPortfolio.setAvgBuyPrice(price);
            stockPortfolio.setLastUpdated(LocalDateTime.now());
            stockPortfolioDAO.save(stockPortfolio);
        }
    }

    /**
     * Helper method to update stock portfolio for a sell transaction
     */
    private void updateStockPortfolioForSell(Portfolio portfolio, Stock stock, int quantity) {
        Optional<StockPortfolio> optionalStockPortfolio = stockPortfolioDAO.findByPortfolio_PortfolioIdAndStock_Symbol(portfolio.getPortfolioId(), stock.getSymbol());

        if (optionalStockPortfolio.isPresent()) {
            StockPortfolio stockPortfolio = optionalStockPortfolio.get();
            int newQuantity = stockPortfolio.getQuantity() - quantity;

            if (newQuantity <= 0) {
                // Remove stock from portfolio if quantity becomes zero or negative
                stockPortfolioDAO.deleteByPortfolio_PortfolioIdAndStock_Symbol(portfolio.getPortfolioId(), stock.getSymbol());
            } else {
                // Update quantity (average buy price stays the same when selling)
                stockPortfolio.setQuantity(newQuantity);
                stockPortfolio.setLastUpdated(LocalDateTime.now());
                stockPortfolioDAO.save(stockPortfolio);
            }
        }
    }

    /**
     * Helper method to update portfolio total value
     */
    private void updatePortfolioTotalValue(Portfolio portfolio) {
        BigDecimal total = BigDecimal.ZERO;

        // Get all stocks in the portfolio
        List<StockPortfolio> stockPortfolios = stockPortfolioDAO.findByPortfolio_PortfolioId(portfolio.getPortfolioId());

        // Calculate total value
        for (StockPortfolio stockPortfolio : stockPortfolios) {
            // Get current price from the stock service
            BigDecimal currentPrice;
            try {
                currentPrice = stockService.getCurrentPrice(stockPortfolio.getStock().getSymbol());
            } catch (Exception e) {
                // If there's an error getting the current price, use the average buy price
                currentPrice = stockPortfolio.getAvgBuyPrice();
            }

            BigDecimal stockValue = currentPrice.multiply(new BigDecimal(stockPortfolio.getQuantity()));
            total = total.add(stockValue);
        }

        // Update portfolio
        portfolio.setTotal(total);
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolioDAO.save(portfolio);
    }
}