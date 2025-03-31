import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import portfolioService from '../../services/portfolioService';
import stockService from '../../services/stockService';
import transactionService from '../../services/transactionService';
import Loading from '../common/Loading';

const ExecuteTransaction = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const user = sessionStorage.getItem('user');
    const userId = user ? JSON.parse(user).id : null;

    // Check if we have pre-filled data from navigation state
    const prefillData = location.state || {};

    const [formData, setFormData] = useState({
        portfolioId: prefillData.portfolioId || '',
        stockSymbol: prefillData.stock?.symbol || '',
        quantity: 1,
        price: prefillData.price || '',
        transactionType: prefillData.action || 'BUY'
    });

    const [portfolios, setPortfolios] = useState([]);
    const [searchResults, setSearchResults] = useState([]);
    const [stockSymbolValid, setStockSymbolValid] = useState(!!prefillData.stock);
    const [loading, setLoading] = useState(false);
    const [initialLoading, setInitialLoading] = useState(true);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                // Fetch user portfolios
                const portfoliosData = await portfolioService.getPortfoliosByUserId(userId);
                setPortfolios(portfoliosData);

                // If we have a stock symbol, validate it
                if (formData.stockSymbol) {
                    const stockData = await stockService.getStockBySymbol(formData.stockSymbol);
                    if (stockData) {
                        setStockSymbolValid(true);
                        if (!formData.price) {
                            // Get current price if not provided
                            const priceData = await stockService.getCurrentPrice(formData.stockSymbol);
                            setFormData(prev => ({...prev, price: priceData}));
                        }
                    }
                }

                setInitialLoading(false);
            } catch (err) {
                console.error('Error fetching initial data:', err);
                setError('Failed to load data. Please try again.');
                setInitialLoading(false);
            }
        };

        fetchInitialData();
    }, [userId, formData.stockSymbol]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });

        // Reset stockSymbolValid if the stock symbol changes
        if (name === 'stockSymbol') {
            setStockSymbolValid(false);
        }
    };

    const handleSearchStock = async () => {
        if (!formData.stockSymbol.trim()) {
            setError('Please enter a stock symbol');
            return;
        }

        try {
            setLoading(true);

            // First try to get the stock directly by symbol
            try {
                const stock = await stockService.getStockBySymbol(formData.stockSymbol);
                if (stock) {
                    setStockSymbolValid(true);
                    // Get current price
                    const price = await stockService.getCurrentPrice(formData.stockSymbol);
                    setFormData(prev => ({...prev, price}));
                    setLoading(false);
                    return;
                }
            } catch (err) {
                // If not found by exact symbol, search
                console.log('Stock not found by exact symbol, searching...');
            }

            // Search by keyword
            const results = await stockService.searchStocks(formData.stockSymbol);
            setSearchResults(results);
            setLoading(false);
        } catch (err) {
            console.error('Error searching stocks:', err);

            if (err.response && err.response.status === 503) {
                setError('API rate limit reached. Please try again later.');
            } else {
                setError('Failed to search stocks. Please try again.');
            }

            setLoading(false);
        }
    };

    const selectStock = async (stock) => {
        try {
            setFormData(prev => ({...prev, stockSymbol: stock.symbol}));
            setStockSymbolValid(true);
            setSearchResults([]);

            // Get current price
            const price = await stockService.getCurrentPrice(stock.symbol);
            setFormData(prev => ({...prev, price}));
        } catch (err) {
            console.error('Error fetching stock price:', err);
            setError('Failed to fetch stock price. Please enter it manually.');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Basic validation
        if (!formData.portfolioId) {
            setError('Please select a portfolio');
            return;
        }

        if (!stockSymbolValid) {
            setError('Please enter a valid stock symbol and verify it');
            return;
        }

        if (!formData.quantity || formData.quantity <= 0) {
            setError('Please enter a valid quantity');
            return;
        }

        if (!formData.price || parseFloat(formData.price) <= 0) {
            setError('Please enter a valid price');
            return;
        }

        try {
            setLoading(true);
            setError(null);

            const transactionData = {
                portfolioId: formData.portfolioId,
                stockSymbol: formData.stockSymbol,
                quantity: parseInt(formData.quantity),
                price: parseFloat(formData.price)
            };

            let result;
            if (formData.transactionType === 'BUY') {
                result = await transactionService.executeBuyTransaction(transactionData);
            } else {
                result = await transactionService.executeSellTransaction(transactionData);
            }

            setSuccess(`Transaction executed successfully!`);
            setTimeout(() => {
                navigate(`/portfolios/${formData.portfolioId}`);
            }, 2000);
        } catch (err) {
            console.error('Error executing transaction:', err);

            if (err.response && err.response.status === 409) {
                setError('Not enough balance or stocks to execute this transaction.');
            } else {
                setError('Failed to execute transaction. Please try again.');
            }

            setLoading(false);
        }
    };

    if (initialLoading) return <Loading />;

    return (
        <div className="execute-transaction">
            <div className="page-header">
                <h1>Execute Transaction</h1>
            </div>

            <div className="card">
                <div className="card-body">
                    {error && (
                        <div className="alert alert-danger">{error}</div>
                    )}

                    {success && (
                        <div className="alert alert-success">{success}</div>
                    )}

                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="transactionType" className="form-label">Transaction Type</label>
                            <select
                                id="transactionType"
                                name="transactionType"
                                className="form-control"
                                value={formData.transactionType}
                                onChange={handleChange}
                            >
                                <option value="BUY">Buy Stock</option>
                                <option value="SELL">Sell Stock</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="portfolioId" className="form-label">Select Portfolio</label>
                            <select
                                id="portfolioId"
                                name="portfolioId"
                                className="form-control"
                                value={formData.portfolioId}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Select a portfolio</option>
                                {portfolios.map((portfolio) => (
                                    <option key={portfolio.portfolioId} value={portfolio.portfolioId}>
                                        {portfolio.name}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="stockSymbol" className="form-label">Stock Symbol</label>
                            <div className="search-input-container">
                                <input
                                    type="text"
                                    id="stockSymbol"
                                    name="stockSymbol"
                                    className="form-control"
                                    value={formData.stockSymbol}
                                    onChange={handleChange}
                                    placeholder="Enter stock symbol (e.g., AAPL)"
                                    required
                                />
                                <button
                                    type="button"
                                    className="btn btn-secondary"
                                    onClick={handleSearchStock}
                                    disabled={loading}
                                >
                                    {loading ? 'Verifying...' : 'Verify Symbol'}
                                </button>
                            </div>
                            {stockSymbolValid && (
                                <div className="alert alert-success mt-2">
                                    Stock symbol verified successfully!
                                </div>
                            )}
                        </div>

                        {searchResults.length > 0 && !stockSymbolValid && (
                            <div className="search-results">
                                <h3>Search Results</h3>
                                <div className="grid">
                                    {searchResults.map((stock) => (
                                        <div key={stock.symbol} className="card stock-card">
                                            <div className="card-header">
                                                <h3>{stock.symbol}</h3>
                                            </div>
                                            <div className="card-body">
                                                <p>{stock.companyName}</p>
                                            </div>
                                            <div className="card-footer">
                                                <button
                                                    type="button"
                                                    className="btn btn-primary"
                                                    onClick={() => selectStock(stock)}
                                                >
                                                    Select
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}

                        <div className="form-group">
                            <label htmlFor="quantity" className="form-label">Quantity</label>
                            <input
                                type="number"
                                id="quantity"
                                name="quantity"
                                className="form-control"
                                value={formData.quantity}
                                onChange={handleChange}
                                min="1"
                                step="1"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="price" className="form-label">Price per Share ($)</label>
                            <input
                                type="number"
                                id="price"
                                name="price"
                                className="form-control"
                                value={formData.price}
                                onChange={handleChange}
                                min="0.01"
                                step="0.01"
                                placeholder="Current market price"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label className="form-label">Transaction Summary</label>
                            <div className="summary-box">
                                <p><strong>Type:</strong> {formData.transactionType}</p>
                                <p><strong>Total Amount:</strong> ${(
                                    parseFloat(formData.price || 0) * parseInt(formData.quantity || 0)
                                ).toFixed(2)}</p>
                            </div>
                        </div>

                        <div className="form-actions">
                            <button
                                type="submit"
                                className="btn btn-primary"
                                disabled={loading || !stockSymbolValid}
                            >
                                {loading ? 'Processing...' : 'Execute Transaction'}
                            </button>
                            <Link to="/transactions" className="btn btn-secondary">
                                Cancel
                            </Link>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default ExecuteTransaction;