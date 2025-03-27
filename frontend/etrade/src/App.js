import './App.css';
import { Route, Routes } from 'react-router-dom';
import StockSearch from './Components/StockSearch';
import Home from './Components/Home';
import Stocks from './Components/Stocks'

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path='/' element={<Home/>} />
        {/* <Route path='/StockSearch' element={<StockSearch/>} /> */}
        <Route path='/Stocks' element={<Stocks />} />
      </Routes>
    </div>
  );
}

export default App;
