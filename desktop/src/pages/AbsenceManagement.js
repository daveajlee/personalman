import React from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {Container, Row, Col, Button} from "react-bootstrap";
import Header from "../components/Header";

function AbsenceManagement() {

    const location = useLocation();
    const months    = ['January','February','March','April','May','June','July','August','September','October','November','December'];
    const navigate = useNavigate();

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
        if ( location.state.month >= 1 ) {
            return months[location.state.month-1];
        } else {
            return months[new Date().getMonth()];
        }
    }

    /**
     * Get the current year which can be influenced by the previous and next month buttons.
     * @returns the current year to be displayed.
     */
    function getYear() {
        if ( location.state.year ) {
            return location.state.year;
        } else {
            return new Date().getFullYear();
        }
    }

    /**
     * Calculate the statistics of the user such as holidays etc.
     */
    function viewStatistics() {
        alert('Statistics is not available');
    }

    /**
     * Add an absence.
     */
    function addAbsence() {
        alert('Add absence is not available');
    }

    /**
     * Move back to the previous month.
     */
    function previousMonth() {
        if ( location.state.month ) {
            if ( location.state.month <= 1 ) {
                location.state.month = 12;
                if ( location.state.year ) {
                    location.state.year -= 1;
                }
            } else {
                location.state.month -= 1;
            }
            if ( location.state.year ) {
                navigate("/absences", {state:{token: location.state.token, month: location.state.month, year: location.state.year }})
            } else {
                navigate("/absences", {state:{token: location.state.token, month: location.state.month, year: new Date().getFullYear() }})
            }
        } else {
            let month = new Date().getMonth() + 1;
            if ( month < 1 ) {
                month = 12;
            } else {
                month -= 1;
            }
            navigate("/absences", {state:{token: location.state.token, month: month, year: new Date().getFullYear() }})
        }
    }

    /**
     * Move forward to the next month.
     */
    function nextMonth() {
        if ( location.state.month ) {
            if ( location.state.month >= 12 ) {
                location.state.month = 1;
                if ( location.state.year ) {
                    location.state.year += 1;
                }
            } else {
                location.state.month += 1;
            }
            if ( location.state.year ) {
                navigate("/absences", {state:{token: location.state.token, month: location.state.month, year: location.state.year }})
            } else {
                navigate("/absences", {state:{token: location.state.token, month: location.state.month, year: new Date().getFullYear() }})
            }
        } else {
            let month = new Date().getMonth() + 1;
            if ( month >= 12 ) {
                month = 1;
            } else {
                month += 1;
            }
            navigate("/absences", {state:{token: location.state.token, month: month, year: new Date().getFullYear() }})
        }
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
                        <h1 className="text-center">{getMonthName()} {getYear()}</h1>
                    </Col>
                </Row>
            </Container>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={addAbsence}>Add Absence</Button>
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={viewStatistics}>View Statistics</Button>
                    </Col>
                </Row>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2 mt-2" size='lg' onClick={previousMonth}>Previous Month</Button>
                        <Button className="mb-0 px-5 me-2 mt-2" size='lg' onClick={nextMonth}>Next Month</Button>
                    </Col>
                </Row>
            </Container>

        </Container>
    )

}

export default AbsenceManagement;
