import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { PieChart, Pie, Tooltip, Cell, ResponsiveContainer, Legend } from 'recharts';
import portfolioService from '../../services/portfolioService';
import stockPortfolioService from '../../services/stockPortfolioService';
import transactionService from '../../services/transactionService';
import Loading from '../common/Loading';

const PortfolioDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [portfolio, setPortfolio] = useState(null);
    const [stocksInPortfolio, setStocksInPortfolio] = useState([]);
    const [recentTransactions, setRecentTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Colors for the pie chart
    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8', '#82ca9d'];

    useEffect(() => {
        const fetchPortfolioDetails = async () => {
            try {
                setLoading(true);

                // Fetch portfolio details
                const portfolioData = await portfolioService.getPortfolioById(id);
                setPortfolio(portfolioData);

                // Fetch stocks in the portfolio
                const stocksData = await stockPortfolioService.getStocksByPortfolioId(id);
                setStocksInPortfolio(stocksData);

                // Fetch recent transactions for this portfolio
                const transactionsData = await transactionService.getPortfolioTransactions(id);
                // Sort by date descending and take the most recent 5
                const sortedTransactions = transactionsData
                    .sort((a, b) => new Date(b.date) - new Date(a.date))
                    .slice(0, 5);
                setRecentTransactions(sortedTransactions);

                setLoading(false);
            } catch (err) {
                console.error('Error fetching portfolio details:', err);
                setError('Failed to load portfolio details. Please try again.');
                setLoading(false);
            }
        };

        fetchPortfolioDetails();
    }, [id]);

    const handleTradeStock = (stock, action) => {
        // Navigate to the transaction page with pre-filled data
        navigate('/transactions/new', {
            state: {
                portfolioId: portfolio.portfolioId,
                stock: stock,
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

    if (!portfolio) {
        return (
            <div className="not-found">
                <h2>Portfolio Not Found</h2>
                <p>The portfolio you are looking for does not exist or you do not have access to it.</p>
                <Link to="/portfolios" className="btn btn-primary">
                    Back to Portfolios
                </Link>
            </div>
        );
    }

    // Prepare data for pie chart
    const pieChartData = stocksInPortfolio.map(stockPortfolio => ({
        name: stockPortfolio.stock.symbol,
        value: stockPortfolio.quantity * stockPortfolio.avgBuyPrice
    }));

    return (
        <div className="portfolio-detail">
            <div className="page-header">
                <h1>{portfolio.name}</h1>
                <p className="portfolio-description">{portfolio.description}</p>
            </div>

            <div className="portfolio-summary card">
                <div className="card-header">
                    <h2>Portfolio Summary</h2>
                </div>
                <div className="card-body">
                    <div className="flex justify-between">
                        <div className="portfolio-info">
                            <p><strong>Total Value:</strong> ${parseFloat(portfolio.total).toFixed(2)}</p>
                            <p><strong>Created:</strong> {new Date(portfolio.createdAt).toLocaleDateString()}</p>
                            <p><strong>Last Updated:</strong> {new Date(portfolio.updatedAt).toLocaleDateString()}</p>
                        </div>
                        <div className="portfolio-chart">
                            {pieChartData.length > 0 ? (
                                <ResponsiveContainer width={300} height={200}>
                                    <PieChart>
                                        <Pie
                                            data={pieChartData}
                                            cx="50%"
                                            cy="50%"
                                            labelLine={false}
                                            outerRadius={80}
                                            fill="#8884d8"
                                            dataKey="value"
                                            label={({ name }) => name}
                                        >
                                            {pieChartData.map((entry, index) => (
                                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                            ))}
                                        </Pie>
                                        <Tooltip formatter={(value) => `$${value.toFixed(2)}`} />
                                        <Legend />
                                    </PieChart>
                                </ResponsiveContainer>
                            ) : (
                                <p>No stocks in portfolio to display chart.</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            <div className="stocks-in-portfolio card">
                <div className="card-header">
                    <h2>Stocks in Portfolio</h2>
                </div>
                <div className="card-body">
                    {stocksInPortfolio.length > 0 ? (
                        <table className="table">
                            <thead>
                            <tr>
                                <th>Symbol</th>
                                <th>Company</th>
                                <th>Quantity</th>
                                <th>Avg. Buy Price</th>
                                <th>Current Value</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {stocksInPortfolio.map((stockPortfolio) => (
                                <tr key={stockPortfolio.stock.symbol}>
                                    <td>{stockPortfolio.stock.symbol}</td>
                                    <td>{stockPortfolio.stock.companyName}</td>
                                    <td>{stockPortfolio.quantity}</td>
                                    <td>${parseFloat(stockPortfolio.avgBuyPrice).toFixed(2)}</td>
                                    <td>${(stockPortfolio.quantity * stockPortfolio.avgBuyPrice).toFixed(2)}</td>
                                    <td>
                                        <button
                                            className="btn btn-success"
                                            onClick={() => handleTradeStock(stockPortfolio.stock, 'BUY')}
                                        >
                                            Buy
                                        </button>
                                        <button
                                            className="btn btn-danger"
                                            onClick={() => handleTradeStock(stockPortfolio.stock, 'SELL')}
                                        >
                                            Sell
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    ) : (
                        <p>No stocks in this portfolio yet. Add some stocks to get started!</p>
                    )}
                    <Link to="/stocks" className="btn btn-primary">
                        Browse Stocks
                    </Link>
                </div>
            </div>

            <div className="recent-transactions card">
                <div className="card-header">
                    <h2>Recent Transactions</h2>
                </div>
                <div className="card-body">
                    {recentTransactions.length > 0 ? (
                        <table className="table">
                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>Stock</th>
                                <th>Type</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Total</th>
                            </tr>
                            </thead>
                            <tbody>
                            {recentTransactions.map((transaction) => (
                                <tr key={transaction.id}>
                                    <td>{new Date(transaction.date).toLocaleDateString()}</td>
                                    <td>{transaction.stock.symbol}</td>
                                    <td className={transaction.transactionType === 'BUY' ? 'text-success' : 'text-danger'}>
                                        {transaction.transactionType}
                                    </td>
                                    <td>{transaction.quantity}</td>
                                    <td>${parseFloat(transaction.price).toFixed(2)}</td>
                                    <td>${(transaction.price * transaction.quantity).toFixed(2)}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    ) : (
                        <p>No recent transactions for this portfolio.</p>
                    )}
                    <Link to={`/transactions/new?portfolioId=${portfolio.portfolioId}`} className="btn btn-primary">
                        New Transaction
                    </Link>
                </div>
            </div>

            <div className="actions">
                <Link to="/portfolios" className="btn btn-secondary">
                    Back to Portfolios
                </Link>
            </div>
        </div>
    );
};

export default PortfolioDetail;