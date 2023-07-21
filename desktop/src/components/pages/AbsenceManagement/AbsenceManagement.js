import React, {useLayoutEffect} from "react";
import {useLocation} from "react-router-dom";
import {Container, Row, Col, Button, Modal, Form} from "react-bootstrap";
import Header from "../../layout/Header/Header";
import {useState} from "react";
import axios from "axios";
import StatisticsModal from "../../modals/StatisticsModal/StatisticsModal";
import AbsenceList from "../../lists/AbsenceList/AbsenceList";
import {useTranslation} from "react-i18next";

/**
 * This is the page which displays the list of absences for the current user and allows them to create new absences or
 * view other months or add an absence.
 * @returns {JSX.Element} to be displayed to the user.
 */
function AbsenceManagement(props) {

    const location = useLocation();

    const [showAddModal, setShowAddModal] = useState(false);
    const [showStatisticsModal, setShowStatisticsModal] = useState(false);

    const {t} = useTranslation();

    /**
     * Function to manage the modal for adding absences.
     */
    const handleAddClose = () => setShowAddModal(false);

    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [reason, setReason] = useState("Illness");

    /**
     * Retrieve the current token either from the supplied state or empty if we are in doc mode.
     */
    const token = (props.docMode && props.docMode==='true') ? "" : location.state.token;

    /**
     * Retrieve the company either from the supplied state or empty if we are in doc mode.
     */
    const company = ( props.docMode && props.docMode==='true') ? "" : location.state.company;

    /**
     * Retrieve the year either from the supplied state or empty if we are in doc mode. 
     */
    const year = ( props.docMode && props.docMode==='true') ? "" :  location.state.year ? location.state.year : new Date().getFullYear();
    
    /**
     * Retrieve the month either from the supplied state or empty if we are in doc mode.
     */
    const month = ( props.docMode && props.docMode==='true') ? "" : location.state.month ? location.state.month : ((new Date().getMonth()) +1);

    /**
     * Load the absences from the REST API either for a particular username within a specific time period
     * (usually a month in a year).
     */
    useLayoutEffect(() => {
        // Load the actual month of absences.
        /*if ( !location.state.month ) {
            setStartDate('01-' + ((new Date().month) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear());
            setEndDate(daysInMonth(((new Date().month) +1), new Date().getFullYear()) + '-' + ((new Date().month) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear());
        } else {*/
            setStartDate('01-' + (month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + year);
            setEndDate(daysInMonth(month, year) + '-' + (month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + year);
        //}

    }, [token, month, year, company]);

    /**
     * Return the number of days that a month has in a particular year.
     * @param month the month to retrieve the number of days for.
     * @param year the year that should be taken into account.
     * @returns {number} the number of days that the month has in the supplied year.
     */
    function daysInMonth (month, year) {
        return new Date(year, month, 0).getDate();
    }

    /**
     * Get the username of the currently logged in user who we will display absences for.
     * @returns the username
     */
    function getUsername() {
        return token.split("-")[0];
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
            alert(t('absenceManagementAddAbsenceFail'));
            return;
        }
        axios.post(process.env.REACT_APP_SERVER_URL + '/absences/', {
            company: company,
            username: getUsername(),
            startDate: startDateSplit[2] + '-' + startDateSplit[1] + '-' + startDateSplit[0],
            endDate: endDateSplit[2] + '-' + endDateSplit[1] + '-' + endDateSplit[0],
            category: reason,
            token: token,
        }).then((response) => {
            if ( response.status === 201 ) {
                alert(t('absenceManagementAddAbsenceSuccess'));
                setShowAddModal(false);
                window.location.reload();
            }
        }).catch((error) => {
            console.error(error);
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

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Container fluid>
            <Header token={token} company={company}/>

            <AbsenceList company={company} token={token} startDate={startDate} endDate={endDate}
            month={month}
            year={year}
            username={getUsername()}/>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={addAbsenceInput}>{t('absenceManagementAddAbsence')}</Button>
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={viewStatistics}>{t('absenceManagementViewStatistics')}</Button>
                    </Col>
                </Row>
            </Container>

            <Modal show={showAddModal} onHide={handleAddClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{t('absenceManagementAddAbsenceModal')}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextStartDate">
                        <Form.Label column sm="2">{t('absenceManagementStartDateModal')}:</Form.Label>
                        <Col sm="10">
                            <Form.Control type="date" value={startDate} placeholder="yyyy-MM-dd" onChange={startDateChangeHandler}/>
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextEndDate">
                        <Form.Label column sm="2">{t('absenceManagementEndDateModal')}:</Form.Label>
                        <Col sm="10">
                            <Form.Control type="date" value={endDate} placeholder="yyyy-MM-dd" onChange={endDateChangeHandler}/>
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextReason">
                        <Form.Label column sm="2">{t('absenceManagementReasonModal')}:</Form.Label>
                        <Col sm="10">
                            <Form.Select aria-label="Role" value={reason} onChange={reasonChangeHandler}>
                                <option key="illness">{t('Illness')}</option>
                                <option key="holiday">{t('Holiday')}</option>
                                <option key="trip">{t('Trip')}</option>
                                <option key="conference">{t('Conference')}</option>
                                <option key="dayinlieu">{t('DayinLieu')}</option>
                                <option key="federalholiday">{t('FederalHoliday')}</option>
                            </Form.Select>
                        </Col>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleAddClose}>
                        {t('cancel')}
                    </Button>
                    <Button variant="primary" onClick={addAbsence}>
                        {t('absenceManagementAddAbsence')}
                    </Button>
                </Modal.Footer>
            </Modal>

            <StatisticsModal year={ year} company={company}
                             token={token} showStatisticsModal={showStatisticsModal} setShowStatisticsModal={setShowStatisticsModal}
                             username={getUsername()}>
            </StatisticsModal>

        </Container>
    )

}

export default AbsenceManagement;
