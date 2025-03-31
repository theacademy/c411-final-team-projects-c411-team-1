import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import userService from '../../services/userService';

const Signup = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        fName: '',
        lName: '',
        email: '',
        password: '',
        balance: '',
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
        if (!formData.fName.trim()) {
            setError('First name is required');
            return;
        }
        if (!formData.lName.trim()) {
            setError('Last name is required');
            return;
        }
        if (!formData.email.trim()) {
            setError('Email address is required');
            return;
        }
        if (formData.email.indexOf('@') === -1 || formData.email.indexOf('.') === -1) {
            setError("Must enter a valid email address");
            return;
        }
        if (!formData.password.trim()) {
            setError('Password is required');
            return;
        }
        if (!formData.balance.trim()) {
            setError('Starting balance is required');
            return;
        }

        try {
            setLoading(true);
            setError(null);

            const userV = {
                name: (formData.fName + " " + formData.lName),
                email: formData.email,
                password: formData.password,
                balance: formData.balance,
            };

            await userService.validateUser(userV);

            await userService.saveUser(userV);

            // Navigate to the login
            navigate('/');
        } catch (err) {
            console.error('Error creating user:', err);
            setError('Error registering user. Please try again.');
            setLoading(false);
        }
    };

    return (
        <div className="Sign up">
            <h1>Signup</h1>
            <div className="singup-box">
                <div className="card-login">
                    <div className="card-header">
                        <h3>Create an Account</h3>
                    </div>
                    <div className="card-body">
                        {error && (
                            <div className="alert alert-danger">{error}</div>
                        )}
                        <div>
                            <form onSubmit={handleSubmit}>
                                <div className="form-group">
                                    <label htmlFor="fName" className="form-label">First Name</label>
                                    <input
                                        type="text"
                                        id="fName"
                                        name="fName"
                                        className="form-control"
                                        value={formData.fName}
                                        onChange={handleChange}
                                        placeholder="Enter first name"
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="lName" className="form-label">Last Name</label>
                                    <input
                                        type="text"
                                        id="lName"
                                        name="lName"
                                        className="form-control"
                                        value={formData.lName}
                                        onChange={handleChange}
                                        placeholder="Enter last name"
                                        required
                                    />
                                </div>
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
                                <div className="form-group">
                                    <label htmlFor="balance" className="form-label">Starting Balance</label>
                                    <input
                                        type='number'
                                        min="0"
                                        step="any"
                                        id="balance"
                                        name="balance"
                                        className="form-control"
                                        value={formData.balance}
                                        onChange={handleChange}
                                        placeholder="Enter starting balance"
                                        required
                                    />
                                </div>
                                <div className="form-actions">
                                    <button
                                        type="submit"
                                        className="btn btn-form-primary"
                                        disabled={loading}
                                    >
                                        {loading ? 'Create Account...' : 'Create Account'}
                                    </button>
                                    <Link to='/' className='btn btn-login-secondary'>
                                        Back to Login
                                    </Link>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Signup