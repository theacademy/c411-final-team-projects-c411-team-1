import apiClient from './api';

// Stock Portfolio service methods
const stockPortfolioService = {
    // Get all stock portfolios
    getAllStockPortfolios: async () => {
        try {
            const response = await apiClient.get('/api/stock-portfolios/all');
            return response.data;
        } catch (error) {
            console.error('Error fetching stock portfolios:', error);
            throw error;
        }
    },

    // Get stocks in a specific portfolio
    getStocksByPortfolioId: async (portfolioId) => {
        try {
            const response = await apiClient.get(`/api/stock-portfolios/portfolio/${portfolioId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching stocks for portfolio ${portfolioId}:`, error);
            throw error;
        }
    },

    // Get portfolios for a specific user
    getUserStockPortfolios: async (userId) => {
        try {
            const response = await apiClient.get(`/api/stock-portfolios/user/${userId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching stock portfolios for user ${userId}:`, error);
            throw error;
        }
    },

    // Get a specific stock in a portfolio
    getStockPortfolio: async (portfolioId, stockSymbol) => {
        try {
            const response = await apiClient.get(`/api/stock-portfolios/${portfolioId}/${stockSymbol}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching stock ${stockSymbol} in portfolio ${portfolioId}:`, error);
            throw error;
        }
    },

    // Add a stock to a portfolio
    addStockToPortfolio: async (stockPortfolioData) => {
        try {
            const response = await apiClient.post('/api/stock-portfolios', stockPortfolioData);
            return response.data;
        } catch (error) {
            console.error('Error adding stock to portfolio:', error);
            throw error;
        }
    },

    // Update a stock in a portfolio
    updateStockInPortfolio: async (stockPortfolioData) => {
        try {
            const response = await apiClient.put('/api/stock-portfolios/update', stockPortfolioData);
            return response.data;
        } catch (error) {
            console.error('Error updating stock in portfolio:', error);
            throw error;
        }
    },

    // Remove a stock from a portfolio
    removeStockFromPortfolio: async (portfolioId, stockSymbol) => {
        try {
            await apiClient.delete(`/api/stock-portfolios/remove/${portfolioId}/${stockSymbol}`);
            return true;
        } catch (error) {
            console.error(`Error removing stock ${stockSymbol} from portfolio ${portfolioId}:`, error);
            throw error;
        }
    },

    // Calculate portfolio value
    calculatePortfolioValue: async (portfolioId) => {
        try {
            const response = await apiClient.get(`/api/stock-portfolios/value/${portfolioId}`);
            return response.data;
        } catch (error) {
            console.error(`Error calculating value for portfolio ${portfolioId}:`, error);
            throw error;
        }
    }
};

export default stockPortfolioService;