import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';

function PortfolioDetails() {
  const { id } = useParams();
  const [portfolio, setPortfolio] = useState(null);

  useEffect(() => {
    fetch(`http://localhost:8080/api/portfolios/${id}`)
      .then(res => res.json())
      .then(data => setPortfolio(data));
  }, [id]);

  if (!portfolio) return <p className="p-6">Loading...</p>;

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-2">{portfolio.name}</h2>
      <p>{portfolio.description}</p>
      <p className="text-gray-600 mt-2">Total Value: ${portfolio.total}</p>
      <p className="text-gray-500">Created at: {new Date(portfolio.createdAt).toLocaleString()}</p>

      <Link to="/portfolios" className="mt-4 inline-block text-blue-600 hover:underline">
        ← Back to Portfolios
      </Link>
    </div>
  );
}

export default PortfolioDetails;
