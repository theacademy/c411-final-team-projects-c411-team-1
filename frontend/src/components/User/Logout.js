// In frontend2/src/components/User/Logout.js
import { useNavigate } from "react-router-dom";
import { useEffect } from 'react';
import authService from '../../services/authService';

const Logout = () => {
    const navigate = useNavigate();

    useEffect(() => {
        authService.logout();
        navigate('/');
    }, [navigate]);

    return null;
};

export default Logout;