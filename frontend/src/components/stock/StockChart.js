import React from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const StockChart = ({ data }) => {
    // Format data for the chart
    const chartData = data.map(item => ({
        date: new Date(item.date).toLocaleDateString(),
        close: parseFloat(item.close),
        open: parseFloat(item.open),
        high: parseFloat(item.high),
        low: parseFloat(item.low)
    }));

    // Function to format the tooltip values
    const formatTooltip = (value) => {
        return `$${value.toFixed(2)}`;
    };

    return (
        <div className="stock-chart-container">
            <ResponsiveContainer width="100%" height={400}>
                <LineChart
                    data={chartData}
                    margin={{
                        top: 5,
                        right: 30,
                        left: 20,
                        bottom: 5,
                    }}
                >
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="date" />
                    <YAxis domain={['auto', 'auto']} />
                    <Tooltip
                        formatter={formatTooltip}
                        labelFormatter={(label) => `Date: ${label}`}
                    />
                    <Legend />
                    <Line
                        type="monotone"
                        dataKey="close"
                        stroke="#0d6efd"
                        activeDot={{ r: 8 }}
                        name="Close Price"
                    />
                    <Line
                        type="monotone"
                        dataKey="open"
                        stroke="#198754"
                        name="Open Price"
                    />
                    <Line
                        type="monotone"
                        dataKey="high"
                        stroke="#dc3545"
                        name="High"
                    />
                    <Line
                        type="monotone"
                        dataKey="low"
                        stroke="#6c757d"
                        name="Low"
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
};

export default StockChart;