import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import portfolioService from '../../services/portfolioService';
import userService from '../../services/userService';

const CreatePortfolio = ({ userId }) => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: '',
        description: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // In CreatePortfolio.js
    useEffect(() => {
        // First check if user exists
        const checkForUsers = async () => {
            try {
                const users = await userService.getAllUsers();

                if (users.length === 0) {
                    // No users exist, create one
                    const newUser = {
                        name: "Test User",
                        email: "test@example.com",
                        password: "password",
                        balance: 1000.00
                    };

                    await userService.saveUser(newUser);
                }
            } catch (err) {
                console.error("Error checking/creating users:", err);
            }
        };

        checkForUsers();
    }, []);
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Basic validation
        if (!formData.name.trim()) {
            setError('Portfolio name is required');
            return;
        }

        try {
            setLoading(true);
            setError(null);

            // Create portfolio object
            const portfolioData = {
                user: { id: userId }, // Assuming backend expects a user object with id
                name: formData.name,
                description: formData.description,
                total: 0, // New portfolio starts with zero value
            };

            // Call the API to create the portfolio
            const createdPortfolio = await portfolioService.createPortfolio(portfolioData);

            // Navigate to the newly created portfolio
            navigate(`/portfolios/${createdPortfolio.portfolioId}`);
        } catch (err) {
            console.error('Error creating portfolio:', err);
            setError('Failed to create portfolio. Please try again.');
            setLoading(false);
        }
    };

    return (
        <div className="create-portfolio">
            <div className="page-header">
                <h1>Create New Portfolio</h1>
            </div>

            <div className="card">
                <div className="card-body">
                    {error && (
                        <div className="alert alert-danger">{error}</div>
                    )}

                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="name" className="form-label">Portfolio Name*</label>
                            <input
                                type="text"
                                id="name"
                                name="name"
                                className="form-control"
                                value={formData.name}
                                onChange={handleChange}
                                placeholder="Enter portfolio name"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="description" className="form-label">Description</label>
                            <textarea
                                id="description"
                                name="description"
                                className="form-control"
                                value={formData.description}
                                onChange={handleChange}
                                placeholder="Enter portfolio description (optional)"
                                rows="3"
                            ></textarea>
                        </div>

                        <div className="form-actions">
                            <button
                                type="submit"
                                className="btn btn-primary"
                                disabled={loading}
                            >
                                {loading ? 'Creating...' : 'Create Portfolio'}
                            </button>
                            <Link to="/portfolios" className="btn btn-secondary">
                                Cancel
                            </Link>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default CreatePortfolio;