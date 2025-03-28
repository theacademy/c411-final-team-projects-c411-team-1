import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import portfolioService from '../../services/portfolioService';
import transactionService from '../../services/transactionService';
import stockService from '../../services/stockService';
import Loading from '../common/Loading';

const Dashboard = () => {
    const [portfolios, setPortfolios] = useState([]);
    const [recentTransactions, setRecentTransactions] = useState([]);
    const [portfolioPerformanceData, setPortfolioPerformanceData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const user = sessionStorage.getItem('user');
    const userId = user ? JSON.parse(user) : null;

    useEffect(() => {
        const fetchDashboardData = async () => {
            try {
                setLoading(true);

                // Fetch user portfolios
                const userPortfolios = await portfolioService.getPortfoliosByUserId(userId);
                setPortfolios(userPortfolios);

                // Fetch recent transactions
                const userTransactions = await transactionService.getUserTransactions(userId);

                // Sort transactions by date and take the 5 most recent
                const sortedTransactions = userTransactions
                    .sort((a, b) => new Date(b.date) - new Date(a.date))
                    .slice(0, 5);

                setRecentTransactions(sortedTransactions);

                // Generate portfolio performance data using transactions
                await generatePortfolioPerformanceData(userTransactions, userPortfolios);

                setLoading(false);
            } catch (err) {
                console.error('Error fetching dashboard data:', err);
                setError('Failed to load dashboard data. Please try again later.');
                setLoading(false);
            }
        };

        fetchDashboardData();
    }, [userId]);

    // Generate portfolio performance data using transactions
    const generatePortfolioPerformanceData = async (transactions, portfolios) => {
        try {
            // Create a map of dates and portfolio values
            if (!transactions || transactions.length === 0) {
                setPortfolioPerformanceData([]);
                return;
            }

            // Sort transactions by date (oldest first)
            const sortedTransactions = [...transactions].sort(
                (a, b) => new Date(a.date) - new Date(b.date)
            );

            // Get the earliest and latest transaction dates
            const earliestDate = new Date(sortedTransactions[0].date);
            const latestDate = new Date();

            // Create an array of months between earliest and latest dates
            const months = [];
            const currentDate = new Date(earliestDate);
            currentDate.setDate(1); // Start from the 1st of the month

            while (currentDate <= latestDate) {
                months.push(new Date(currentDate));
                currentDate.setMonth(currentDate.getMonth() + 1);
            }

            // Initialize a running value that will be updated as we process transactions
            let runningValue = 0;

            // Create performance data points
            const performanceData = [];

            for (const month of months) {
                const monthEnd = new Date(month);
                monthEnd.setMonth(monthEnd.getMonth() + 1);
                monthEnd.setDate(0); // Last day of the month

                // Calculate portfolio value at the end of this month
                // by processing all transactions up to this point
                const relevantTransactions = sortedTransactions.filter(
                    transaction => new Date(transaction.date) <= monthEnd
                );

                // Recalculate portfolio value based on transactions
                // This is a simplified approach - in real life you would track individual stock prices
                let monthValue = 0;

                // If we have portfolio data, use it for the most recent month
                if (month.getMonth() === latestDate.getMonth() &&
                    month.getFullYear() === latestDate.getFullYear() &&
                    portfolios && portfolios.length > 0) {
                    // Use the current portfolio total values
                    monthValue = portfolios.reduce(
                        (total, portfolio) => total + parseFloat(portfolio.total || 0), 0
                    );
                } else {
                    // For past months, estimate based on transactions
                    // Group transactions by portfolio and stock
                    const portfolioStocks = {};

                    for (const transaction of relevantTransactions) {
                        const portfolioId = transaction.portfolio?.portfolioId;
                        const stockSymbol = transaction.stock?.symbol;

                        if (!portfolioId || !stockSymbol) continue;

                        if (!portfolioStocks[portfolioId]) {
                            portfolioStocks[portfolioId] = {};
                        }

                        if (!portfolioStocks[portfolioId][stockSymbol]) {
                            portfolioStocks[portfolioId][stockSymbol] = {
                                quantity: 0,
                                avgPrice: 0,
                                totalCost: 0
                            };
                        }

                        const stockInfo = portfolioStocks[portfolioId][stockSymbol];

                        if (transaction.transactionType === 'BUY') {
                            const transactionValue = transaction.quantity * parseFloat(transaction.price);
                            const newTotalCost = stockInfo.totalCost + transactionValue;
                            const newQuantity = stockInfo.quantity + transaction.quantity;

                            stockInfo.quantity = newQuantity;
                            stockInfo.totalCost = newTotalCost;
                            stockInfo.avgPrice = newTotalCost / newQuantity;
                        } else if (transaction.transactionType === 'SELL') {
                            // For sells, we reduce quantity but maintain the average price
                            stockInfo.quantity -= transaction.quantity;

                            // If quantity becomes 0 or negative, reset avgPrice and totalCost
                            if (stockInfo.quantity <= 0) {
                                stockInfo.quantity = 0;
                                stockInfo.avgPrice = 0;
                                stockInfo.totalCost = 0;
                            }
                        }
                    }

                    // Calculate total value based on quantities and average prices
                    for (const portfolioId in portfolioStocks) {
                        for (const stockSymbol in portfolioStocks[portfolioId]) {
                            const stockInfo = portfolioStocks[portfolioId][stockSymbol];
                            monthValue += stockInfo.quantity * stockInfo.avgPrice;
                        }
                    }
                }

                performanceData.push({
                    name: month.toLocaleDateString('default', { month: 'short', year: 'numeric' }),
                    value: monthValue,
                    date: month // Keep the actual date for sorting
                });
            }

            // Sort by date to ensure chronological order
            performanceData.sort((a, b) => a.date - b.date);

            // Remove the date field which was only used for sorting
            const finalData = performanceData.map(({ name, value }) => ({ name, value }));

            setPortfolioPerformanceData(finalData);
        } catch (error) {
            console.error('Error generating portfolio performance data:', error);
            // Fallback to empty data
            setPortfolioPerformanceData([]);
        }
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

    // Calculate total portfolio value
    const totalPortfolioValue = portfolios.reduce(
        (total, portfolio) => total + parseFloat(portfolio.total || 0),
        0
    );

    return (
        <div className="dashboard">
            <h1>Dashboard</h1>

            <div className="dashboard-summary">
                <div className="card">
                    <div className="card-header">
                        <h3>Portfolio Summary</h3>
                    </div>
                    <div className="card-body">
                        <p><strong>Total Value:</strong> ${totalPortfolioValue.toFixed(2)}</p>
                        <p><strong>Number of Portfolios:</strong> {portfolios.length}</p>
                        <Link to="/portfolios" className="btn btn-primary">View All Portfolios</Link>
                    </div>
                </div>
            </div>

            <div className="dashboard-chart">
                <div className="card">
                    <div className="card-header">
                        <h3>Portfolio Performance Over Time</h3>
                    </div>
                    <div className="card-body">
                        {portfolioPerformanceData.length > 0 ? (
                            <ResponsiveContainer width="100%" height={300}>
                                <LineChart data={portfolioPerformanceData}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip
                                        formatter={(value) => [`$${value.toFixed(2)}`, 'Portfolio Value']}
                                    />
                                    <Legend />
                                    <Line
                                        type="monotone"
                                        dataKey="value"
                                        stroke="#0d6efd"
                                        activeDot={{ r: 8 }}
                                        name="Portfolio Value"
                                    />
                                </LineChart>
                            </ResponsiveContainer>
                        ) : (
                            <div className="text-center p-4">
                                <p>No performance data available yet. Start trading to see your portfolio growth!</p>
                            </div>
                        )}
                    </div>
                </div>
            </div>

            <div className="dashboard-recent-transactions">
                <div className="card">
                    <div className="card-header">
                        <h3>Recent Transactions</h3>
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
                                        <td>{transaction.stock?.symbol}</td>
                                        <td className={transaction.transactionType === 'BUY' ? 'text-success' : 'text-danger'}>
                                            {transaction.transactionType}
                                        </td>
                                        <td>{transaction.quantity}</td>
                                        <td>${parseFloat(transaction.price).toFixed(2)}</td>
                                        <td>${(parseFloat(transaction.price) * transaction.quantity).toFixed(2)}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        ) : (
                            <p>No recent transactions. Start trading to see your transaction history!</p>
                        )}
                        <Link to="/transactions" className="btn btn-primary">View All Transactions</Link>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;