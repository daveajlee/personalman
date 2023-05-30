import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {Container, Row, Col, Button, Modal, Form} from "react-bootstrap";
import Header from "../components/Header";
import {useState} from "react";
import axios from "axios";

function AbsenceManagement() {

    const location = useLocation();
    const months    = ['January','February','March','April','May','June','July','August','September','October','November','December'];
    const navigate = useNavigate();

    const [showAddModal, setShowAddModal] = useState(false);
    const [showStatisticsModal, setShowStatisticsModal] = useState(false);
    const handleAddClose = () => setShowAddModal(false);
    const handleStatisticsClose = () => setShowStatisticsModal(false);

    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [reason, setReason] = useState("Illness");

    const [absences, setAbsences] = useState([]);

    useEffect(() => {
        let startDate, endDate;
        if ( !location.state.month ) {
            startDate= '01-' + ((new Date().getMonth()) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear();
            endDate = daysInMonth(((new Date().getMonth()) +1), new Date().getFullYear()) + '-' + ((new Date().getMonth()) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear();
        } else {
            startDate= '01-' + (location.state.month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + location.state.year;
            endDate = daysInMonth(location.state.month, location.state.year) + '-' + (location.state.month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + location.state.year;
        }
        axios.get('http://localhost:8150/api/absences/?company=' + location.state.company + '&username=' + location.state.token.split("-")[0] + '&startDate=' + startDate + '&endDate=' + endDate + '&onlyCount=false&token=' + location.state.token)
            .then(res => {
                const result = res.data;
                setAbsences(result['absenceResponseList']);
            })
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
            }
        }).catch(function (error) {
            console.log(error);
        });
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
                navigate("/absences", {state:{token: location.state.token, month: location.state.month, year: location.state.year, company: location.state.company }})
            } else {
                navigate("/absences", {state:{token: location.state.token, month: location.state.month, year: new Date().getFullYear(), company: location.state.company }})
            }
        } else {
            let month = new Date().getMonth() + 1;
            if ( month < 1 ) {
                month = 12;
            } else {
                month -= 1;
            }
            navigate("/absences", {state:{token: location.state.token, month: month, year: new Date().getFullYear(), company: location.state.company }})
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
                navigate("/absences", {state:{token: location.state.token, month: location.state.month, year: location.state.year, company: location.state.company }})
            } else {
                navigate("/absences", {state:{token: location.state.token, month: location.state.month, year: new Date().getFullYear(), company: location.state.company }})
            }
        } else {
            let month = new Date().getMonth() + 1;
            if ( month >= 12 ) {
                month = 1;
            } else {
                month += 1;
            }
            navigate("/absences", {state:{token: location.state.token, month: month, year: new Date().getFullYear(), company: location.state.company }})
        }
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
        <Container>
            <Header token={location.state.token}/>

            <Container fluid className="p-3 my-5 h-custom">
                <Row className="d-flex flex-row align-items-center justify-content-center">
                    <Col>
                        <h1 className="text-center">Absences for user: {getUsername()}</h1>
                    </Col>
                </Row>
                <Row className="d-flex flex-row align-items-center justify-content-center mb-5">
                    <Col>
                        <h1 className="text-center">{getMonthName()} {getYear()}</h1>
                    </Col>
                </Row>
                {absences.map(d => (<Row className="d-flex flex-row align-items-center justify-content-center" key={d.startDate + '-' + d.endDate}>
                    <Col>
                        <h3 className="text-center">{d.startDate} to {d.endDate} with {d.category} </h3>
                    </Col>
                </Row>))}
            </Container>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={addAbsenceInput}>Add Absence</Button>
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

            <Modal show={showStatisticsModal} onHide={handleStatisticsClose}>
                <Modal.Header closeButton>
                    <Modal.Title>View Statistics</Modal.Title>
                </Modal.Header>
                <Modal.Body>Woohoo, you're reading this text in a modal!</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleStatisticsClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleStatisticsClose}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>

        </Container>
    )

}

export default AbsenceManagement;
