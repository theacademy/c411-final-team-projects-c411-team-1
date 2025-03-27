import React, { useState } from 'react'

function Stocks() {

    const [stockList, setStockList] = useState([]);
    const [showList, setShowList] = useState(false);
    const [stockName, setStockName] = useState("");
    const [stockCompany, setStockCompany] = useState("");

    //handle when input is changing (allows user to type in input)
    const stockNameInput = event => {
        setStockName(event.target.value);
    }
    const stockCompanyInput = event => {
        setStockCompany(event.target.value);
    }

    //handle the button click events
    const handleRefresh = () => {
        setShowList(!showList)
    }

    const handleGetStocks = async() => {    //calls getStock api from backend
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
                    "symbol": "aaple",
                    "companyName": "Apple Inc"
                })
              })

            if(!response.created) {
                console.log(response.status)
                throw new Error("Error creating object ${response.status}");
            }

        } catch(err) {
            console.log(err.message)
        }
    }

    const handleConsole = () => {
        console.log(stockList)
        console.log(stockList[0])
        console.log(stockList[0].companyName)
    }

    return (
        <div className="flex flex-col">
            <div>
                Test
            </div>
            <div className='flex flex-row justify-center gap-4'>
                <button className="" onClick={handleGetStocks}>
                    Get All Stocks
                </button>
                <button className='' onClick={handleRefresh}>
                    Refresh
                </button>
            </div>
            <button className='' onClick={handleConsole}>
                get console
            </button>
            <div className='flex flex-row justify-center'>
                <label className='flex mr-4'>Enter stock symbol</label>
                <input
                    className='mr-4 border'
                    placeholder='Enter stock symbol here'
                    value={stockName}
                    onChange={stockNameInput}
                />
                <label className='flex mr-4'>Enter company name</label>
                <input
                    className='mr-4 border'
                    placeholder='Enter company name here'
                    value={stockCompany}
                    onChange={stockCompanyInput}
                />
                <button className='flex' onClick={handleAddStock}>
                    add Stock
                </button>
            </div>
            <div>
                Results
            </div>
            <div>
                { showList && (
                    <div className='flex flex-col'>
                        {stockList.map((stock) => (
                                <div>
                                    {stock.companyName}: {stock.symbol}
                                </div>
                            ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default Stocks