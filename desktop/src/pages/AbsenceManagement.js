import React from "react";
import {useLocation} from "react-router-dom";
import {Container, Row, Col, Button} from "react-bootstrap";
import Header from "../components/Header";

function AbsenceManagement() {

    const location = useLocation();

    /**
     * Get the username of the currently logged in user who we will display absences for.
     * @returns the username
     */
    function getUsername() {
        return location.state.token.split("-")[0];
    }

    /**
     * Get the name of the current month that we are in.
     * @returns the name of the month in English
     */
    function getMonthName() {
        let months    = ['January','February','March','April','May','June','July','August','September','October','November','December'];
        return months[new Date().getMonth()];
    }

    /**
     * Calculate the statistics of the user such as holidays etc.
     */
    function viewStatistics() {
        alert('Statistics is not available');
    }

    return (
        <Container>
            <Header token={location.state.token}/>

            <Container fluid className="p-3 my-5 h-custom">
                <Row className="d-flex flex-row align-items-center justify-content-center">
                    <Col>
                        <h1 className="text-center">Absences for user: {getUsername()}</h1>
                    </Col>
                </Row>
                <Row className="d-flex flex-row align-items-center justify-content-center">
                    <Col>
                        <h1 className="text-center">{getMonthName()} {new Date().getFullYear()}</h1>
                    </Col>
                </Row>
            </Container>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={viewStatistics}>View Statistics</Button>
                    </Col>
                </Row>
            </Container>

        </Container>
    )

}

export default AbsenceManagement;
