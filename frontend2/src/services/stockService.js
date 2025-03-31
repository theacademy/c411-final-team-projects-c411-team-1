import apiClient from './api';

// Stock service methods
const stockService = {
    // Get all stocks
    getAllStocks: async () => {
        try {
            const response = await apiClient.get('/api/stocks');
            return response.data;
        } catch (error) {
            console.error('Error fetching stocks:', error);
            throw error;
        }
    },

    // Get stock by symbol
    getStockBySymbol: async (symbol) => {
        try {
            const response = await apiClient.get(`/api/stocks/${symbol}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching stock ${symbol}:`, error);
            throw error;
        }
    },

    // Search stocks by keyword
    searchStocks: async (keyword) => {
        try {
            const response = await apiClient.get(`/api/stocks/search/${keyword}`);
            return response.data;
        } catch (error) {
            console.error(`Error searching stocks with keyword ${keyword}:`, error);
            throw error;
        }
    },

    // Get current price for a stock
    getCurrentPrice: async (symbol) => {
        try {
            const response = await apiClient.get(`/api/stocks/price/${symbol}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching price for stock ${symbol}:`, error);
            throw error;
        }
    },

    // Get historical data for a stock
    getStockHistory: async (symbol, startDate, endDate) => {
        try {
            const response = await apiClient.get(`/api/stocks/history/${symbol}/${startDate}&${endDate}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching history for stock ${symbol}:`, error);
            throw error;
        }
    }
};

export default stockService;