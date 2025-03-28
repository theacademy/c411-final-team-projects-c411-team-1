import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import userService from '../../services/userService';
import Loading from '../common/Loading';



const Login = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [userId, setUserId] = useState(0);  //hard coded for now

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

            //gets user with certain email
            const user = await userService.getUser(formData.email);
            sessionStorage.setItem('user', JSON.stringify(user.id));

            // Navigate to the dashboard
            navigate(`/dashboard`);
        } catch (err) {
            console.error('Error validating user:', err);
            setError('Incorrect username or password. Please try again.');
            setLoading(false);
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