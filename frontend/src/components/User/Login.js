// In frontend2/src/components/User/Login.js
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import authService from '../../services/authService';
import Loading from '../common/Loading';

const Login = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

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

            await authService.login(formData.email, formData.password);

            // Navigate to the dashboard
            navigate('/dashboard');
        } catch (err) {
            console.error('Error logging in:', err);
            setError('Incorrect username or password. Please try again.');
            setLoading(false);
        }
    };

    if (loading) return <Loading />;

    return (
        <div className="login">
            <h1>Login</h1>
            <div className="login-box">
                <div className="card-login">
                    <div className="card-header">
                        <h3>Login Below</h3>
                        <h4>Don't have an account yet? 
                            <Link to='/signup' className='login-link'>
                                Sign Up
                            </Link>
                        </h4>
                    </div>
                    <div className="card-body">
                        {error && (
                            <div className="alert alert-danger">{error}</div>
                        )}
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
                                />
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
};

export default Login;