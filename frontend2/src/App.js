import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
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

function App() {
    // For demo purposes, we'll use a hardcoded user ID
    const [currentUserId] = useState(1);

    return (
        <Router>
            <div className="app-container">
                <Navbar />
                <main className="main-content">
                    <Routes>
                        <Route path="/" element={<Dashboard userId={currentUserId} />} />
                        <Route path="/portfolios" element={<PortfolioList userId={currentUserId} />} />
                        <Route path="/portfolios/new" element={<CreatePortfolio userId={currentUserId} />} />
                        <Route path="/portfolios/:id" element={<PortfolioDetail />} />
                        <Route path="/stocks" element={<StockSearch />} />
                        <Route path="/stocks/:symbol" element={<StockDetail />} />
                        <Route path="/transactions" element={<TransactionList userId={currentUserId} />} />
                        <Route path="/transactions/new" element={<ExecuteTransaction userId={currentUserId} />} />
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
}

export default App;