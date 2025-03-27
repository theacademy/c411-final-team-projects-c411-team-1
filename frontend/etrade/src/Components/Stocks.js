import React, { useState } from 'react'

function Stocks() {

    const [stockList, setStockList] = useState([]);
    const [stockName, setStockName] = useState("");
    const [stockCompany, setStockCompany] = useState("");

    //handle when input is changing (allows user to type in input)
    const stockNameInput = event => {
        setStockName(event.target.value);
    }
    const stockCompanyInput = event => {
        setStockCompany(event.target.value);
    }

    const handleRefresh = async() => {    //calls getStock api from backend
        try {
            const data = await (await fetch("http://localhost:8080/api/stocks")).json()
            setStockList(data)
        } catch(err) {
            console.log(err.message)
        }
    }

    const handleAddStock = async() => {    //calls createStock api from backend
        try {
            const response = await fetch('http://localhost:8080/api/stocks', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    "symbol": stockName,
                    "companyName": stockCompany
                })
              })

            if(!response.ok) {
                console.log(response.status)
                throw new Error("Error adding stock");
            }

        } catch(err) {
            console.log(err.message)
        }
    }

    return (
        <div className="flex flex-col">
            <div className='mb-10'>
                Stocks
            </div>
            <button className='' onClick={handleRefresh}>
                Refresh
            </button>
            <div className='flex flex-col justify-center w-full mb-10'>
                <label className='flex mr-4 justify-center'>Enter stock symbol</label>
                <div className='flex justify-center'>
                    <input
                        className='flex border w-3/4 text-center'
                        placeholder='Enter stock symbol here'
                        value={stockName}
                        onChange={stockNameInput}
                    />
                </div>
                <label className='flex mr-4 justify-center'>Enter company name</label>
                <div className='flex justify-center'>
                    <input
                        className='flex border w-3/4 text-center'
                        placeholder='Enter company name here'
                        value={stockCompany}
                        onChange={stockCompanyInput}
                    />
                </div>
                <div className='flex w-full justify-center'>
                    <button className='flex justify-center mt-4 border w-20' onClick={handleAddStock}>
                        add Stock
                    </button>
                </div>
            </div>
            <div>
                Results
            </div>
            <div>
                <div className='flex flex-col'>
                    {stockList.map((stock, i) => (
                            <div>
                                {stock.companyName}: {stock.symbol}
                            </div>
                        ))}
                </div>
            </div>
        </div>
    );
}

export default Stocks