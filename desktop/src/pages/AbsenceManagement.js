import React from "react";
import {useLocation} from "react-router-dom";
import {Container} from "react-bootstrap";

function AbsenceManagement() {

    const location = useLocation();

    return (
        <Container>
            <h1>Absence Management - Coming Soon!</h1>

            <h3>{location.state.token}</h3>
        </Container>
    )

}

export default AbsenceManagement;
