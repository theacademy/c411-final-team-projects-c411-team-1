import './App.css';
import { Route, Routes } from 'react-router-dom';
import StockSearch from './Components/StockSearch';
import Home from './Components/Home';
import Stocks from './Components/Stocks'
import PortfolioList from './Components/PortfolioList';
import PortfolioDetails from './Components/PortfolioDetails';
import AddPortfolioForm from './Components/AddPortfolioForm';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path='/' element={<Home/>} />
        {/* <Route path='/StockSearch' element={<StockSearch/>} /> */}
        <Route path='/Stocks' element={<Stocks />} />
        <Route path='/portfolios' element={<PortfolioList />} />
        <Route path='/portfolios/add' element={<AddPortfolioForm />} />
        <Route path='/portfolios/:id' element={<PortfolioDetails />} />
      </Routes>
    </div>
  );
}

export default App;
