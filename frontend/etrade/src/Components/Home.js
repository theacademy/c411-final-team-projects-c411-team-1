import { Link } from "react-router-dom";

function Home() {
  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <h1 className="text-3xl font-bold mb-6 text-center">E-Trade Home</h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 justify-items-center">
        <Link to="/stocks">
          <button className="w-48 bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 px-6 rounded shadow">
            Get Stocks
          </button>
        </Link>

        <Link to="/portfolios">
          <button className="w-48 bg-green-500 hover:bg-green-600 text-white font-semibold py-3 px-6 rounded shadow">
            View Portfolios
          </button>
        </Link>

        <Link to="/portfolios/add">
          <button className="w-48 bg-purple-500 hover:bg-purple-600 text-white font-semibold py-3 px-6 rounded shadow">
            Add Portfolio
          </button>
        </Link>
      </div>
    </div>
  );
}

export default Home;
