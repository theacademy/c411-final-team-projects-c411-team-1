import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import portfolioService from '../../services/portfolioService';
import Loading from '../common/Loading';

const PortfolioList = ({ userId }) => {
    const [portfolios, setPortfolios] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPortfolios = async () => {
            try {
                setLoading(true);
                const data = await portfolioService.getPortfoliosByUserId(userId);
                setPortfolios(data);
                setLoading(false);
            } catch (err) {
                console.error('Error fetching portfolios:', err);
                setError('Failed to load portfolios. Please try again.');
                setLoading(false);
            }
        };

        fetchPortfolios();
    }, [userId]);

    const handleDeletePortfolio = async (portfolioId) => {
        if (window.confirm('Are you sure you want to delete this portfolio?')) {
            try {
                await portfolioService.deletePortfolio(portfolioId);
                // Update the portfolios list after deletion
                setPortfolios(portfolios.filter(portfolio => portfolio.portfolioId !== portfolioId));
            } catch (err) {
                console.error('Error deleting portfolio:', err);
                setError('Failed to delete portfolio. Please try again.');
            }
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

    return (
        <div className="portfolio-list">
            <div className="page-header flex justify-between items-center">
                <h1>My Portfolios</h1>
                <Link to="/portfolios/new" className="btn btn-primary">
                    Create New Portfolio
                </Link>
            </div>

            {portfolios.length === 0 ? (
                <div className="empty-state">
                    <p>You don't have any portfolios yet.</p>
                    <Link to="/portfolios/new" className="btn btn-primary">
                        Create Your First Portfolio
                    </Link>
                </div>
            ) : (
                <div className="grid">
                    {Array.isArray(portfolios) ?
                        portfolios.map((portfolio) => (
                            <div key={portfolio.portfolioId} className="card">
                                <div className="card-header">
                                    <h3>{portfolio.name}</h3>
                                </div>
                                <div className="card-body">
                                    <p>{portfolio.description || 'No description provided.'}</p>
                                    <p><strong>Total Value:</strong> ${parseFloat(portfolio.total).toFixed(2)}</p>
                                    <p><strong>Created:</strong> {new Date(portfolio.createdAt).toLocaleDateString()}</p>
                                    <p><strong>Last Updated:</strong> {new Date(portfolio.updatedAt).toLocaleDateString()}</p>
                                </div>
                                <div className="card-footer">
                                    <Link
                                        to={`/portfolios/${portfolio.portfolioId}`}
                                        className="btn btn-secondary"
                                    >
                                        View Details
                                    </Link>
                                    <button
                                        onClick={() => handleDeletePortfolio(portfolio.portfolioId)}
                                        className="btn btn-danger"
                                    >
                                        Delete
                                    </button>
                                </div>
                            </div>
                        )) :
                        <p>No portfolios available</p>
                    }
                </div>
            )}
        </div>
    );
};

export default PortfolioList;