import React from 'react';

const Footer = () => {
    const year = new Date().getFullYear();

    return (
        <footer className="footer">
            <div className="footer-content">
                <p>&copy; {year} ETrade Application. All rights reserved.</p>
            </div>
        </footer>
    );
};

export default Footer;