import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import stockService from '../../services/stockService';
import portfolioService from '../../services/portfolioService';
import Loading from '../common/Loading';
import StockChart from './StockChart';

const StockDetail = () => {
    const { symbol } = useParams();
    const navigate = useNavigate();
    const [stock, setStock] = useState(null);
    const [currentPrice, setCurrentPrice] = useState(null);
    const [stockHistory, setStockHistory] = useState([]);
    const [portfolios, setPortfolios] = useState([]);
    const [selectedPortfolio, setSelectedPortfolio] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Get dates for historical data
    const today = new Date();
    const oneMonthAgo = new Date();
    oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);

    const formatDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    useEffect(() => {
        const fetchStockData = async () => {
            try {
                setLoading(true);

                // Fetch stock details
                const stockData = await stockService.getStockBySymbol(symbol);
                setStock(stockData);

                // Fetch current price
                const priceData = await stockService.getCurrentPrice(symbol);
                setCurrentPrice(priceData);

                // Fetch historical data
                const historyData = await stockService.getStockHistory(
                    symbol,
                    formatDate(oneMonthAgo),
                    formatDate(today)
                );
                setStockHistory(historyData);

                // Fetch user portfolios for the transaction form
                // In a real app, you'd filter by user ID
                const portfoliosData = await portfolioService.getAllPortfolios();
                setPortfolios(portfoliosData);

                setLoading(false);
            } catch (err) {
                console.error('Error fetching stock data:', err);

                // Check if it's an API limit error
                if (err.response && err.response.status === 503) {
                    setError('API rate limit reached. Please try again later.');
                } else {
                    setError('Failed to load stock data. Please try again.');
                }

                setLoading(false);
            }
        };

        fetchStockData();
    }, [symbol]);

    const handlePortfolioChange = (e) => {
        setSelectedPortfolio(e.target.value);
    };

    const handleNavigateToTransaction = (action) => {
        if (!selectedPortfolio) {
            alert('Please select a portfolio');
            return;
        }

        // Navigate to transaction page with pre-filled data
        navigate('/transactions/new', {
            state: {
                portfolioId: selectedPortfolio,
                stock: stock,
                price: currentPrice,
                action: action
            }
        });
    };

    if (loading) return <Loading />;

    if (error) {
        return (
            <div className="error-container">
                <p className="error-message">{error}</p>
                <button
                    className="btn btn-primary"
                    onClick={() => window.location.reload()}
                >
                    Retry
                </button>
            </div>
        );
    }

    if (!stock) {
        return (
            <div className="not-found">
                <h2>Stock Not Found</h2>
                <p>The stock symbol you are looking for does not exist or could not be found.</p>
                <Link to="/stocks" className="btn btn-primary">
                    Back to Stock Search
                </Link>
            </div>
        );
    }

    return (
        <div className="stock-detail">
            <div className="page-header">
                <h1>{stock.companyName} ({stock.symbol})</h1>
            </div>

            <div className="stock-info card">
                <div className="card-header">
                    <h2>Stock Information</h2>
                </div>
                <div className="card-body">
                    <div className="stock-summary">
                        <p><strong>Symbol:</strong> {stock.symbol}</p>
                        <p><strong>Company:</strong> {stock.companyName}</p>
                        <p><strong>Current Price:</strong> ${currentPrice ? parseFloat(currentPrice).toFixed(2) : 'N/A'}</p>
                    </div>

                    <div className="stock-transaction">
                        <h3>Trade This Stock</h3>
                        <div className="form-group">
                            <label htmlFor="portfolio" className="form-label">Select Portfolio</label>
                            <select
                                id="portfolio"
                                className="form-control"
                                value={selectedPortfolio}
                                onChange={handlePortfolioChange}
                            >
                                <option value="">Select a portfolio</option>
                                {portfolios.map((portfolio) => (
                                    <option key={portfolio.portfolioId} value={portfolio.portfolioId}>
                                        {portfolio.name}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="trade-actions">
                            <button
                                className="btn btn-success"
                                onClick={() => handleNavigateToTransaction('BUY')}
                                disabled={!selectedPortfolio}
                            >
                                Buy Stock
                            </button>
                            <button
                                className="btn btn-danger"
                                onClick={() => handleNavigateToTransaction('SELL')}
                                disabled={!selectedPortfolio}
                            >
                                Sell Stock
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div className="stock-chart card">
                <div className="card-header">
                    <h2>Price History (Last 30 Days)</h2>
                </div>
                <div className="card-body">
                    {stockHistory && stockHistory.length > 0 ? (
                        <StockChart data={stockHistory} />
                    ) : (
                        <p>No historical data available for this stock.</p>
                    )}
                </div>
            </div>

            <div className="actions">
                <Link to="/stocks" className="btn btn-secondary">
                    Back to Stock Search
                </Link>
            </div>
        </div>
    );
};

export default StockDetail;