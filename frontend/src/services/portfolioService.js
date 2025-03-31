import apiClient from './api';

// Portfolio service methods
const portfolioService = {
    // Get all portfolios
    getAllPortfolios: async () => {
        try {
            const response = await apiClient.get('/api/portfolios');
            return response.data;
        } catch (error) {
            console.error('Error fetching portfolios:', error);
            throw error;
        }
    },

    // Get portfolios for a specific user
    getPortfoliosByUserId: async (userId) => {
        try {
            const response = await apiClient.get(`/api/portfolios/user/${userId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching portfolios for user ${userId}:`, error);
            throw error;
        }
    },

    // Get a specific portfolio by ID
    getPortfolioById: async (portfolioId) => {
        try {
            const response = await apiClient.get(`/api/portfolios/${portfolioId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching portfolio ${portfolioId}:`, error);
            throw error;
        }
    },

    // Create a new portfolio
    createPortfolio: async (portfolioData) => {
        try {
            const response = await apiClient.post('/api/portfolios', portfolioData);
            return response.data;
        } catch (error) {
            console.error('Error creating portfolio:', error);
            throw error;
        }
    },

    // Update an existing portfolio
    updatePortfolio: async (portfolioId, portfolioData) => {
        try {
            const response = await apiClient.put(`/api/portfolios/${portfolioId}`, portfolioData);
            return response.data;
        } catch (error) {
            console.error(`Error updating portfolio ${portfolioId}:`, error);
            throw error;
        }
    },

    // Delete a portfolio
    deletePortfolio: async (portfolioId) => {
        try {
            await apiClient.delete(`/api/portfolios/${portfolioId}`);
            return true;
        } catch (error) {
            console.error(`Error deleting portfolio ${portfolioId}:`, error);
            throw error;
        }
    },

    // Get a portfolio's total value
    getPortfolioTotalValue: async (portfolioId) => {
        try {
            const response = await apiClient.get(`/api/portfolios/${portfolioId}/total`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching total value for portfolio ${portfolioId}:`, error);
            throw error;
        }
    }
};

export default portfolioService;