// Create a new file at: frontend2/src/services/userService.js

import apiClient from './api';

const userService = {
    getAllUsers: async () => {
        try {
            const response = await apiClient.get('/api/users');
            return response.data;
        } catch (error) {
            console.error('Error fetching users:', error);
            throw error;
        }
    },

    getUser: async (userEmail) => {
        try {
            const response = await apiClient.get('/api/users/email/' + userEmail);
            return response.data;
        } catch (error) {
            console.error('Error fetching user:', error);
            throw error;
        }
    },

    saveUser: async (userData) => {
        try {
            const response = await apiClient.post('/api/users', userData);
            return response.data;
        } catch (error) {
            console.error('Error creating user:', error);
            throw error;
        }
    },

    validateUser: async (userData) => {
        try {
            const response = await apiClient.put('/api/users/validate', userData);
            return response.data;
        } catch (error) {
            console.error('Error validating user:', error);
            throw error;
        }
    }
};

export default userService;