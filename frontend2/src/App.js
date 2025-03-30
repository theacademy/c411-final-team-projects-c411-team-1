import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, BrowserRouter } from 'react-router-dom';
import Navbar from './components/common/Navbar';
import Footer from './components/common/Footer';
import Dashboard from './components/dashboard/Dashboard';
import PortfolioList from './components/portfolio/PortfolioList';
import PortfolioDetail from './components/portfolio/PortfolioDetail';
import CreatePortfolio from './components/portfolio/CreatePortfolio';
import StockSearch from './components/stock/StockSearch';
import StockDetail from './components/stock/StockDetail';
import TransactionList from './components/transaction/TransactionList';
import ExecuteTransaction from './components/transaction/ExecuteTransaction';
import Login from './components/User/Login'
import Logout from './components/User/Logout'
import Signup from './components/User/Signup'

function App() {
    // For demo purposes, we'll use a hardcoded user ID
    const [currentUserId] = useState(1);

    return (
        <BrowserRouter>
            <div className="app-container">
                <Navbar />
                <main className="main-content">
                    <Routes>
                        <Route path="/" element={<Login />} />
                        <Route path="/logout" element={<Logout />} />
                        <Route path="/signup" element={<Signup />} />
                        <Route path="/dashboard" element={<Dashboard />} />
                        <Route path="/portfolios" element={<PortfolioList />} />
                        <Route path="/portfolios/new" element={<CreatePortfolio />} />
                        <Route path="/portfolios/:id" element={<PortfolioDetail />} />
                        <Route path="/stocks" element={<StockSearch />} />
                        <Route path="/stocks/:symbol" element={<StockDetail />} />
                        <Route path="/transactions" element={<TransactionList />} />
                        <Route path="/transactions/new" element={<ExecuteTransaction />} />
                    </Routes>
                </main>
                <Footer />
            </div>
        </BrowserRouter>
    );
}

export default App;