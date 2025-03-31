import { Link } from 'react-router-dom';

const Navbar = () => {    
    return (
        <nav className="navbar">
            <div className="navbar-container">
                <Link to="/dashboard" className="navbar-logo">
                    <h2>ETrade</h2>
                </Link>

                <ul className="nav-menu">
                    <li className="nav-item">
                        <Link to="/dashboard" className="nav-link">Dashboard</Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/portfolios" className="nav-link">Portfolios</Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/stocks" className="nav-link">Stocks</Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/transactions" className="nav-link">Transactions</Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/logout" className="nav-link">Login/Logout</Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
};

export default Navbar;