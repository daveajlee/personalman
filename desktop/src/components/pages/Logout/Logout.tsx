import {useEffect} from "react";
import axios from "axios";
import {useLocation, useNavigate} from "react-router-dom";
import * as React from "react";

type LogoutProps = {
    docMode: string;
};

/**
 * This component shows the user that we are logging them out of the system. Afterwards it redirects to the start page.
 */
function Logout({docMode}: LogoutProps): React.JSX.Element {

    const location = useLocation();
    const navigate = useNavigate();

    /**
     * Retrieve the current token either from the supplied state or empty if we are in doc mode.
     */
    const token = (docMode && docMode==='true') ? "" : location.state.token;

    /**
     * Logout of the system via the REST API.
     */
    useEffect(() => {
        axios.post(import.meta.env.VITE_SERVER_URL + `/user/logout`, {
            token: token
        })
            .then(res => {
                if ( res.status === 200 ) {
                    navigate("/")
                }
            }).catch(error => {
                console.error(error);
        })
    }, [token, navigate]);

    /**
     * Show the user that we are trying to log them out...
     */
    return (
        <h1>We are logging you out of the system. Please wait...</h1>
    )

}

export default Logout;
