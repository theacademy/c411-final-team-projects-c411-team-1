import React, { useState, useEffect } from 'react'

function StockSearch () {
    const [keywords, setKeywords] = useState("")
    const [stockList, setStockList] = useState(null);

    const keywordInput = event => {   //handle when input is changing (allows user to type in input)
        setKeywords(event.target.value);
    }

    const handleSearch = async() => {
        try {
            const data = await (await fetch("http://localhost:8080/api/stocks/search/" + keywords)).json()
            setStockList(data)
        } catch(err) {
            console.log(err.message)
        }
    }

    return (
        <div className="flex flex-col h-max w-max justify-normal">
            <div className="flex mt-10 ml-5 justify-items-center align-middle">
                <div className="justify-normal">
                    <label class="text-lg font-medium text-gray-700 text-center align-middle">Stock keywords: </label>
                </div>
                <input
                    className='border-2 border-blue-100 rounded-xl p-3 mt-1 ml-3 bg-transparent'
                    placeholder='Enter keywords here'
                    value={keywords}
                    onChange={keywordInput}
                />
            </div>
            <div className='flex justify-center ml-5 h-12 mt-5'>
            <button fontfamily="Arial" type="submit" class="hover:bg-gray-600 rounded-md text-xl pt-3 pr-3 pb-3 pl-3
                            bg-gray-800 font-semibold text-white w-40 text-center" onClick={handleSearch}>Search</button>
                        <div class="items-center justify-start mt-6 mr-0 mb-0 ml-0 pt-6 pr-0 pb-0 pl-0 flex border-t-2
                            border-gray-100">
                        </div>
            </div>
            <div className='flex ml-5 mt-10'>
                Result
            </div>
        </div>
    );
}

export default StockSearch;