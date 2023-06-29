import React, {useEffect} from "react";
import axios from 'axios';
import {useLocation, useNavigate} from "react-router-dom";

/**
 * This component shows the user that we are logging them out of the system. Afterwards it redirects to the start page.
 */
function Logout() {

    const location = useLocation();
    const navigate = useNavigate();

    /**
     * Logout of the system via the REST API.
     */
    useEffect(() => {
        axios.post(process.env.REACT_APP_SERVER_URL + `/user/logout`, {
            token: location.state.token
        })
            .then(res => {
                if ( res.status === 200 ) {
                    navigate("/")
                }
            })
    }, [location.state.token, navigate]);

    /**
     * Show the user that we are trying to log them out...
     */
    return (
        <h1>We are logging you out of the system. Please wait...</h1>
    )

}

export default Logout;
