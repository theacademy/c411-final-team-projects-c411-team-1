// In frontend2/src/services/authService.js
import apiClient from './api';

const authService = {
    login: async (email, password) => {
        try {
            const response = await apiClient.post('/api/auth/login', {
                email: email,
                password: password
            });

            // Store user info and token in session storage
            sessionStorage.setItem('user', JSON.stringify(response.data.user));
            sessionStorage.setItem('token', response.data.token);

            return response.data;
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    },

    logout: () => {
        sessionStorage.removeItem('user');
        sessionStorage.removeItem('token');
    },

    getCurrentUser: () => {
        return JSON.parse(sessionStorage.getItem('user'));
    },

    isAuthenticated: () => {
        return sessionStorage.getItem('token') !== null;
    }
};

export default authService;