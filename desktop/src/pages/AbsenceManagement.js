import React, {useLayoutEffect} from "react";
import {useLocation} from "react-router-dom";
import {Container, Row, Col, Button, Modal, Form} from "react-bootstrap";
import Header from "../components/Header";
import {useState} from "react";
import axios from "axios";
import StatisticsModal from "../components/StatisticsModal";
import AbsenceList from "../components/AbsenceList";

function AbsenceManagement() {

    const location = useLocation();

    const [showAddModal, setShowAddModal] = useState(false);
    const [showStatisticsModal, setShowStatisticsModal] = useState(false);
    const handleAddClose = () => setShowAddModal(false);

    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [reason, setReason] = useState("Illness");

    useLayoutEffect(() => {
        // Load the actual month of absences.
        if ( !location.state.month ) {
            setStartDate('01-' + ((new Date().getMonth()) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear());
            setEndDate(daysInMonth(((new Date().getMonth()) +1), new Date().getFullYear()) + '-' + ((new Date().getMonth()) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear());
        } else {
            setStartDate('01-' + (location.state.month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + location.state.year);
            setEndDate(daysInMonth(location.state.month, location.state.year) + '-' + (location.state.month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + location.state.year);
        }

    }, [location.state.token, location.state.month, location.state.year, location.state.company]);

    function daysInMonth (month, year) {
        return new Date(year, month, 0).getDate();
    }

    /**
     * Get the username of the currently logged in user who we will display absences for.
     * @returns the username
     */
    function getUsername() {
        return location.state.token.split("-")[0];
    }

    /**
     * Calculate the statistics of the user such as holidays etc.
     */
    function viewStatistics() {
        setShowStatisticsModal(true);
    }

    /**
     * Show the input modal to add an absence.
     */
    function addAbsenceInput() {
        setShowAddModal(true);
    }

    /**
     * Add the actual absence.
     */
    function addAbsence() {
        let startDateSplit = startDate.split("-");
        let endDateSplit = endDate.split("-");
        if ( endDateSplit[2] < startDateSplit[2] && endDateSplit[1] <= startDateSplit[1] && endDateSplit[0] <= startDateSplit[0] ) {
            alert('Not possible to add date since end date is before start date');
            return;
        }
        axios.post('http://localhost:8150/api/absences/', {
            company: location.state.company,
            username: getUsername(),
            startDate: startDateSplit[2] + '-' + startDateSplit[1] + '-' + startDateSplit[0],
            endDate: endDateSplit[2] + '-' + endDateSplit[1] + '-' + endDateSplit[0],
            category: reason,
            token: location.state.token,
        }).then(function (response) {
            if ( response.status === 201 ) {
                alert('Absence was added successfully!');
                setShowAddModal(false);
                window.location.reload();
            }
        }).catch(function (error) {
            console.log(error);
        });
    }

    /**
     * Set the start date that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function startDateChangeHandler(event) {
        setStartDate(event.target.value);
    }

    /**
     * Set the end date that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function endDateChangeHandler(event) {
        setEndDate(event.target.value);
    }

    /**
     * Set the reason that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function reasonChangeHandler(event) {
        setReason(event.target.value);
    }

    return (
        <Container fluid>
            <Header token={location.state.token} company={location.state.company}/>

            <AbsenceList company={location.state.company} token={location.state.token} startDate={startDate} endDate={endDate}
            month={location.state.month ? location.state.month : ((new Date().getMonth()) +1)}
            year={location.state.year ? location.state.year : new Date().getFullYear()}
            username={getUsername()}/>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={addAbsenceInput}>Add Absence</Button>
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={viewStatistics}>View Statistics</Button>
                    </Col>
                </Row>
            </Container>

            <Modal show={showAddModal} onHide={handleAddClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add an Absence</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextStartDate">
                        <Form.Label column sm="2">Start Date:</Form.Label>
                        <Col sm="10">
                            <Form.Control type="date" value={startDate} placeholder="yyyy-MM-dd" onChange={startDateChangeHandler}/>
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextEndDate">
                        <Form.Label column sm="2">End Date:</Form.Label>
                        <Col sm="10">
                            <Form.Control type="date" value={endDate} placeholder="yyyy-MM-dd" onChange={endDateChangeHandler}/>
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextReason">
                        <Form.Label column sm="2">Reason:</Form.Label>
                        <Col sm="10">
                            <Form.Select aria-label="Role" value={reason} onChange={reasonChangeHandler}>
                                <option key="illness">Illness</option>
                                <option key="holiday">Holiday</option>
                                <option key="trip">Trip</option>
                                <option key="conference">Conference</option>
                                <option key="dayinlieu">Day in Lieu</option>
                                <option key="federalholiday">Federal Holiday</option>
                            </Form.Select>
                        </Col>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleAddClose}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={addAbsence}>
                        Add Absence
                    </Button>
                </Modal.Footer>
            </Modal>

            <StatisticsModal year={ location.state.year ? location.state.year : new Date().getFullYear()} company={location.state.company}
                             token={location.state.token} showStatisticsModal={showStatisticsModal} setShowStatisticsModal={setShowStatisticsModal}
                             username={getUsername()}>
            </StatisticsModal>

        </Container>
    )

}

export default AbsenceManagement;
