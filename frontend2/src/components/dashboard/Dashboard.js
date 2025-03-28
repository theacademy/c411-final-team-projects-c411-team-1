import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import portfolioService from '../../services/portfolioService';
import transactionService from '../../services/transactionService';
import Loading from '../common/Loading';

const Dashboard = ({ userId }) => {
    const [portfolios, setPortfolios] = useState([]);
    const [recentTransactions, setRecentTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Mock data for the chart (in case no real data)
    const portfolioPerformanceData = [
        { name: 'Jan', value: 4000 },
        { name: 'Feb', value: 3000 },
        { name: 'Mar', value: 5000 },
        { name: 'Apr', value: 2780 },
        { name: 'May', value: 1890 },
        { name: 'Jun', value: 2390 },
        { name: 'Jul', value: 3490 },
    ];

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
                setLoading(false);
            } catch (err) {
                console.error('Error fetching dashboard data:', err);
                setError('Failed to load dashboard data. Please try again later.');
                setLoading(false);
            }
        };

        fetchDashboardData();
    }, [userId]);

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
    const totalPortfolioValue = (() => {
        console.log('Portfolios type:', typeof portfolios);
        console.log('Portfolios:', portfolios);

        if (!Array.isArray(portfolios)) {
            return 0;
        }

        return portfolios.reduce((total, portfolio) =>
            total + (parseFloat(portfolio.total) || 0), 0);
    })();

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
                        <h3>Portfolio Performance</h3>
                    </div>
                    <div className="card-body">
                        <ResponsiveContainer width="100%" height={300}>
                            <LineChart data={portfolioPerformanceData}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="name" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Line type="monotone" dataKey="value" stroke="#0d6efd" activeDot={{ r: 8 }} />
                            </LineChart>
                        </ResponsiveContainer>
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