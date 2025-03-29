import { useNavigate } from "react-router-dom";
import { useEffect } from 'react';

const Logout = () => {
    const navigate = useNavigate();

    useEffect(() => {
        sessionStorage.setItem('user', null);
        navigate('/');
    });

    return (
        <div></div>
    );
}

export default Logout