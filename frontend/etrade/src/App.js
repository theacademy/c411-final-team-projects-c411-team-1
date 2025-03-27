import './App.css';
import { Route, Routes } from 'react-router-dom';
import StockSearch from './Components/StockSearch';
import Home from './Components/Home';
import Stocks from './Components/Stocks'
import Portfolio from './Components/Portfolio';
import PortfolioDetails from './Components/PortfolioDetails';
import AddPortfolio from './Components/AddPortfolio';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path='/' element={<Home/>} />
        {/* <Route path='/StockSearch' element={<StockSearch/>} /> */}
        <Route path='/Stocks' element={<Stocks />} />
        <Route path='/portfolios' element={<Portfolio />} />
        <Route path='/portfolios/add' element={<AddPortfolio />} />
        <Route path='/portfolios/:id' element={<PortfolioDetails />} />
      </Routes>
    </div>
  );
}

export default App;
