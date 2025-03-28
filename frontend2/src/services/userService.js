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

    saveUser: async (userData) => {
        try {
            const response = await apiClient.post('/api/users', userData);
            return response.data;
        } catch (error) {
            console.error('Error creating user:', error);
            throw error;
        }
    }
};

export default userService;