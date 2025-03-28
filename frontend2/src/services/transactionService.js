import apiClient from './api';

// Transaction service methods
const transactionService = {
    // Get all transactions
    getAllTransactions: async () => {
        try {
            const response = await apiClient.get('/api/transactions');
            return response.data;
        } catch (error) {
            console.error('Error fetching transactions:', error);
            throw error;
        }
    },

    // Get transaction by ID
    getTransactionById: async (id) => {
        try {
            const response = await apiClient.get(`/api/transactions/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching transaction ${id}:`, error);
            throw error;
        }
    },

    // Get transactions for a specific portfolio
    getPortfolioTransactions: async (portfolioId) => {
        try {
            const response = await apiClient.get(`/api/transactions/portfolio/${portfolioId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching transactions for portfolio ${portfolioId}:`, error);
            throw error;
        }
    },

    // Get transactions for a specific user
    // Get transactions for a specific user
    async getUserTransactions(userId) {
        try {
            const response = await apiClient.get(`/api/transactions/user/${userId}`);

            // Optional: Transform transactions if needed
            return response.data.map(transaction => ({
                ...transaction,
                date: transaction.date ? new Date(transaction.date).toISOString() : null
            }));
        } catch (error) {
            console.error('Error fetching user transactions:', error);
            throw error;
        }
    },

    // Execute a buy transaction
    executeBuyTransaction: async (transactionData) => {
        try {
            const response = await apiClient.post('/api/transactions/buy', transactionData);
            return response.data;
        } catch (error) {
            console.error('Error executing buy transaction:', error);
            throw error;
        }
    },

    // Execute a sell transaction
    executeSellTransaction: async (transactionData) => {
        try {
            const response = await apiClient.post('/api/transactions/sell', transactionData);
            return response.data;
        } catch (error) {
            console.error('Error executing sell transaction:', error);
            throw error;
        }
    }
};

export default transactionService;