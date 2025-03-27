import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function PortfolioList() {
  const [portfolios, setPortfolios] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/api/portfolios/user/1') // hardcoded for now
      .then(res => res.json())
      .then(data => setPortfolios(data));
  }, []);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Your Portfolios</h2>
      {portfolios.length === 0 ? (
        <p>No portfolios found.</p>
      ) : (
        <ul className="space-y-4">
          {portfolios.map(p => (
            <li key={p.portfolioId} className="border p-4 rounded shadow">
              <h3 className="text-xl font-semibold">{p.name}</h3>
              <p>{p.description}</p>
              <p className="text-sm text-gray-600">Total Value: ${p.total}</p>
              <Link
                to={`/portfolios/${p.portfolioId}`}
                className="inline-block mt-2 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
              >
                View Details
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default PortfolioList;
