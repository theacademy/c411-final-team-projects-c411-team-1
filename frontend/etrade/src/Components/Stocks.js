import React, { useState } from 'react'

function Stocks() {

    const [stockList, setStockList] = useState([]);
    const [showList, setShowList] = useState(false);
    const [stockName, setStockName] = useState("");
    const [stockCompany, setStockCompany] = useState("");

    const handleGetStocks = async() => {
        try {
            const data = await (await fetch("http://localhost:8080/api/stocks")).json()
            setStockList(data)
            setShowList(!showList)
        } catch(err) {
            console.log(err.message)
        }
    }

    const stockNameInput = event => {   //handle when input is changing (allows user to type in input)
        setStockName(event.target.value);
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
            <button className="" onClick={handleGetStocks}>
                Get All Stocks
            </button>
            {/* <button className='' onClick={handleConsole}>
                get console
            </button> */}
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
                    placeholder='Enter stock name here'
                    value={stockName}
                    onChange={stockNameInput}
                />
                <button className='flex'>
                    add Stock
                </button>
            </div>
            <div>
                Results
            </div>
            <div>
                { showList && (
                    <div className='flex flex-row'>
                        {stockList.map((stock) => (
                                <div>
                                    {stock.symbol} {stock.companyName}
                                </div>
                            ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default Stocks