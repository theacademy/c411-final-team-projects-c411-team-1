import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import userService from '../../services/userService';


const Login = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const userId = 1;  //hard coded for now
    sessionStorage.setItem('user', JSON.stringify(userId));

    useEffect(() => {

    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Basic validation
        if (!formData.email.trim()) {
            setError('Email address is required');
            return;
        }
        if (!formData.password.trim()) {
            setError('Password is required');
            return;
        }

        try {
            setLoading(true);
            setError(null);

            // // Create user object
            // const userData = {
            //     name: formData.name,
            //     email: formData.email,
            //     password: formData.password,
            //     balance: formData.balance
            // };

            // // Call the API to create the portfolio
            // const createdPortfolio = await portfolioService.createPortfolio(portfolioData);

            // Navigate to the dashboard
            navigate(`/dashboard`);
        } catch (err) {
            console.error('Error validating user:', err);
            setError('Incorrect username or password. Please try again.');
            setLoading(false);
        }
    };

    return (
        <div className="login">
                    <h1>Login</h1>     
                    <div className="login-box">
                        <div className="card-login">
                            <div className="card-header">
                                <h3>Login Below</h3>
                                <h4>Don't have an account yet? Sign Up</h4> 
                            </div>
                            <div className="card-body">
                                <form onSubmit={handleSubmit}>
                                    <div className="form-group">
                                        <label htmlFor="email" className="form-label">Email Address</label>
                                        <input
                                            type="text"
                                            id="email"
                                            name="email"
                                            className="form-control"
                                            value={formData.email}
                                            onChange={handleChange}
                                            placeholder="Enter email address"
                                            required
                                            />
                                    </div>
                                    <div className="form-group">
                                        <label htmlFor="password" className="form-label">Password</label>
                                        <input
                                            type='password'
                                            id="password"
                                            name="password"
                                            className="form-control"
                                            value={formData.password}
                                            onChange={handleChange}
                                            placeholder="Enter password"
                                            required
                                            ></input>
                                        </div>
                                    <div className="form-actions">
                                        <button
                                            type="submit"
                                            className="btn btn-primary"
                                            disabled={loading}
                                        >
                                        {loading ? 'Login...' : 'Login'}
                                        </button>
                                        </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
    );

}
export default Login