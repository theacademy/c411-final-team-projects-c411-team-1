// In frontend/src/services/api.js
import axios from 'axios';

const API_URL = 'http://localhost:8080'; // Remove /api here

// Create axios instance with base URL
const apiClient = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default apiClient;