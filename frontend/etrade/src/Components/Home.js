import { Link } from "react-router-dom";

function Home () {
    return (
        <div className="App">
            <div>
                <header>Home Page</header>
                {/* <Link to="/StockSearch" className=''>
                    <button className=''>Search Stocks</button>
                </Link> */}
                <Link to="/Stocks" className=''>
                    <button className=''>Get Stocks</button>
                </Link>
            </div>
        </div>
    );
}

export default Home;