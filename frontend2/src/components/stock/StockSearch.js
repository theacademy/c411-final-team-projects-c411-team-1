import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import stockService from '../../services/stockService';
import Loading from '../common/Loading';

const StockSearch = () => {
    const [keyword, setKeyword] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [hasSearched, setHasSearched] = useState(false);

    const handleSearchChange = (e) => {
        setKeyword(e.target.value);
    };

    const handleSearch = async (e) => {
        e.preventDefault();

        if (!keyword.trim()) {
            setError('Please enter a search term');
            return;
        }

        try {
            setLoading(true);
            setError(null);

            const results = await stockService.searchStocks(keyword);
            setSearchResults(results);
            setHasSearched(true);
            setLoading(false);
        } catch (err) {
            console.error('Error searching stocks:', err);

            // Check if it's an API limit error
            if (err.response && err.response.status === 503) {
                setError('API rate limit reached. Please try again later.');
            } else {
                setError('Failed to search stocks. Please try again.');
            }

            setLoading(false);
        }
    };

    return (
        <div className="stock-search">
            <div className="page-header">
                <h1>Stock Search</h1>
            </div>

            <div className="search-container card">
                <div className="card-body">
                    {error && (
                        <div className="alert alert-danger">{error}</div>
                    )}

                    <form onSubmit={handleSearch} className="search-form">
                        <div className="form-group">
                            <label htmlFor="keyword" className="form-label">Search for Stocks</label>
                            <div className="search-input-container">
                                <input
                                    type="text"
                                    id="keyword"
                                    className="form-control"
                                    value={keyword}
                                    onChange={handleSearchChange}
                                    placeholder="Enter company name or symbol"
                                />
                                <button
                                    type="submit"
                                    className="btn btn-primary"
                                    disabled={loading}
                                >
                                    {loading ? 'Searching...' : 'Search'}
                                </button>
                            </div>
                        </div>
                    </form>

                    {loading ? (
                        <Loading />
                    ) : (
                        hasSearched && (
                            <div className="search-results">
                                <h3>Search Results</h3>

                                {searchResults.length === 0 ? (
                                    <p>No stocks found for "{keyword}". Try another search term.</p>
                                ) : (
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
                                                    <Link
                                                        to={`/stocks/${stock.symbol}`}
                                                        className="btn btn-secondary"
                                                    >
                                                        View Details
                                                    </Link>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                        )
                    )}
                </div>
            </div>
        </div>
    );
};

export default StockSearch;