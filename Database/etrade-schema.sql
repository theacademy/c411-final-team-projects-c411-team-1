drop database if exists etrade;
create database etrade;

use etrade;

create table user (
	id INT auto_increment,
    name VARCHAR(50),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(15,2) NOT NULL,
    CONSTRAINT pk_user
		primary key (id) );

create table stock (
	stock_symbol VARCHAR(10),
    company_name VARCHAR(25) NOT NULL,
    CONSTRAINT pk_stock
		primary key (stock_symbol) );

create table portfolio (
	portfolio_id INT auto_increment,
    user_id INT NOT NULL,
    name VARCHAR(50),
    description VARCHAR(255),
    total DECIMAL(15,2) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT pk_portfolio
		primary key (portfolio_id),
	CONSTRAINT fk_portfolio_user
		foreign key (user_id)
        references user(id) );

create table stock_portfolio (
	stock_symbol VARCHAR(10),
    portfolio_id INT,
    quantity INT NOT NULL,
    average_buy_price DECIMAL(15,2),
    last_updated DATETIME,
    CONSTRAINT pk_stock_portfolio
		primary key (stock_symbol, portfolio_id),
	CONSTRAINT fk_stock_portfolio_stock
		foreign key (stock_symbol)
        references stock(stock_symbol),
	CONSTRAINT fk_stock_portfolio_portfolio
		foreign key (portfolio_id)
        references portfolio(portfolio_id)
        ON DELETE CASCADE );

create table transactions (
	transaction_id INT auto_increment,
    stock_symbol VARCHAR(10) NOT NULL,
    portfolio_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    transaction_date DATETIME NOT NULL,
    type VARCHAR(5) NOT NULL,
    CONSTRAINT pk_transactions
		primary key (transaction_id),
	CONSTRAINT fk_transactions_stock
		foreign key (stock_symbol)
        references stock(stock_symbol),
	CONSTRAINT fk_transactions_portfolio
		foreign key (portfolio_id)
        references portfolio(portfolio_id)
        ON DELETE CASCADE );