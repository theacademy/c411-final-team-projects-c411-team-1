import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import transactionService from '../../services/transactionService';
import Loading from '../common/Loading';

const TransactionList = () => {
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const user = sessionStorage.getItem('user');
    const userId = user ? JSON.parse(user) : null;

    useEffect(() => {
        const fetchTransactions = async () => {
            try {
                setLoading(true);
                const data = await transactionService.getUserTransactions(userId);
                // Sort transactions by date (newest first)
                const sortedData = data.sort((a, b) => new Date(b.date) - new Date(a.date));
                setTransactions(sortedData);
                setLoading(false);
            } catch (err) {
                console.error('Error fetching transactions:', err);
                setError('Failed to load transactions. Please try again.');
                setLoading(false);
            }
        };

        fetchTransactions();
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

    return (
        <div className="transaction-list">
            <div className="page-header flex justify-between items-center">
                <h1>Transaction History</h1>
                <Link to="/transactions/new" className="btn btn-primary">
                    New Transaction
                </Link>
            </div>

            {transactions.length === 0 ? (
                <div className="empty-state">
                    <p>You don't have any transactions yet.</p>
                    <Link to="/transactions/new" className="btn btn-primary">
                        Execute Your First Transaction
                    </Link>
                </div>
            ) : (
                <div className="card">
                    <div className="card-body">
                        <table className="table">
                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>Portfolio</th>
                                <th>Stock</th>
                                <th>Type</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Total</th>
                            </tr>
                            </thead>
                            <tbody>
                            {transactions.map((transaction) => (
                                <tr key={transaction.id}>
                                    <td>{new Date(transaction.date).toLocaleString()}</td>
                                    <td>
                                        <Link to={`/portfolios/${transaction.portfolio.portfolioId}`}>
                                            {transaction.portfolio.name}
                                        </Link>
                                    </td>
                                    <td>
                                        <Link to={`/stocks/${transaction.stock.symbol}`}>
                                            {transaction.stock.symbol}
                                        </Link>
                                    </td>
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
                    </div>
                </div>
            )}
        </div>
    );
};

export default TransactionList;