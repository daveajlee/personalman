import {useState} from "react";
import {Button, Col, Container, Form, Image, Row} from "react-bootstrap";
import {useLocation, useNavigate} from "react-router-dom";
import Header from "../../layout/Header/Header";
import {useTranslation} from "react-i18next";
import logo from './../../../assets/personalman-logo.png';
import * as React from "react";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import { registerLocale, setDefaultLocale } from  "react-datepicker";
import { de } from 'date-fns/locale/de';
registerLocale('de', de)
setDefaultLocale('de');

type ProfilePageProps = {
    docMode: string;
}

/**
 * This is the page which displays the profile of the user.
 * @returns {JSX.Element} to be displayed to the user.
 */
function ProfilePage ({docMode}: ProfilePageProps): React.JSX.Element {

    const [trainingCourse, setTrainingCourse] = useState("");
    const [timesheetDate, setTimesheetDate] = useState(new Date());
    const [hours, setHours] = useState("0");
    const navigate = useNavigate();
    const location = useLocation();

    const {t} = useTranslation();

    /**
     * Retrieve the current token either from the supplied state or empty if we are in doc mode.
     * @returns the current token as a string.
     */
    function getToken() {
        if ( docMode && docMode==='true') {
            return " - ";
        } else {
            return location.state.token;
        }
    }

    /**
     * Retrieve the company either from the supplied state or empty if we are in doc mode.
     * @returns the current company as a string.
     */
    function getCompany() {
        if ( docMode && docMode==='true') {
            return "";
        } else {
            return location.state.company;
        }
    }

    /**
     * Set the training course that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function trainingCourseChangeHandler(event: React.ChangeEvent<HTMLInputElement>) {
        setTrainingCourse(event.target.value);
    }

    /**
     * Set the timesheet entry that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function timesheetChangeHandler(event: React.ChangeEvent<HTMLInputElement>) {
        setHours(event.target.value);
    }

    /**
     *  Add the hours to the timesheet by sending the request to the API.
     *  Display an alert that the hours were added successfully or error message.
     */
    function addHoursToTimesheet() {
        var hoursNum = parseInt(hours);
        if ( isNaN(hoursNum) || hoursNum < 1 || hoursNum > 24 ) {
            alert(t('timesheetTooBigOrSmall'));
            return;
        }
        fetch(import.meta.env.VITE_SERVER_URL + '/user/timesheet', {
            method: 'PATCH',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                company: "",
                username: getUsername(),
                date: convertDateFormat(),
                hours: hoursNum
            })
        }).then(function (response) {
            if ( response.status === 200 ) {
                alert(t('addTimesheetSuccess'));
                navigate("/absences", {state:{token: getToken(), company: getCompany(), username: getUsername()}})
            }
        }).catch(function (error) {
            alert(t('addTimesheetError' + error));
        });
    }

    function convertDateFormat() {
        return addZeroPrefix(timesheetDate.getDate()) + "-" + addZeroPrefix((timesheetDate.getMonth()+1)) + "-" + timesheetDate.getFullYear();
    }

    function addZeroPrefix(num: number) {
        return num < 10 ? "0" + num : num;
    }

    /**
     * Method to reset form to initial values.
     */
    function resetForm () {
        setTrainingCourse("");
    }

    /**
     *  Change the password by sending the request to the API assuming that the new and confirmed password
     *  are the same. Display an alert with either password changed successfully or error message.
     */
    function addTrainingCourse() {
        fetch(import.meta.env.VITE_SERVER_URL + '/user/training', {
            method: 'PATCH',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                company: "",
                username: getUsername(),
                trainingCourse: trainingCourse
            })
        }).then(function (response) {
            if ( response.status === 200 ) {
                alert(t('addTrainingCourseSuccess'));
                navigate("/absences", {state:{token: getToken(), company: getCompany(), username: getUsername()}})
            }
        }).catch(function (error) {
            alert(t('addTrainingCourseError' + error));
        });
    }

    /**
     * Get the username of the currently logged in user who we will display.
     * @returns the username
     */
    function getUsername() {
        return location.state.username;
    }

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Container fluid>
            <Header token={getToken()} company={getCompany()} username={getUsername()}/>
        <Container fluid className="p-3 my-5 h-custom">
        <Row className="d-flex flex-row align-items-center justify-content-center">
            <Col>
                <Image src={logo}
                       className="d-block mx-auto img-fluid w-50" alt="PersonalMan Logo"/>
            </Col>
            <Col>
                <Container className="d-flex flex-row align-items-center justify-content-center mb-3">
                    <Row>
                        <Col>
                            <h3>{t('trainingCourseTitle')} {getUsername()}</h3>
                        </Col>
                    </Row>
                </Container>

                <Container className="d-flex flex-column">
                    <Form>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextTrainingCourse">
                            <Form.Label column sm="2">{t('addTrainingCourse')}:</Form.Label>
                            <Col sm="10">
                                <Form.Control value={trainingCourse} placeholder={t('trainingCourseName')} onChange={trainingCourseChangeHandler}/>
                            </Col>
                        </Form.Group>

                    </Form>
                </Container>

                <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                    <Row>
                        <Col className="text-center">
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={addTrainingCourse}>{t('trainingCourseButton')}</Button>
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={resetForm}>{t('trainingCourseReset')}</Button>
                        </Col>
                    </Row>
                </Container>

                <br/><br/>

                <Container className="d-flex flex-row align-items-center justify-content-center mb-3">
                    <Row>
                        <Col>
                            <h3>{t('timesheetTitle')} {getUsername()}</h3>
                        </Col>
                    </Row>
                </Container>

                <Container className="d-flex flex-column">
                    <Form>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextTimesheetDate">
                            <Form.Label column sm="2">{t('timesheetDate')}:</Form.Label>
                            <Col sm="10">
                                <DatePicker locale="de" selected={timesheetDate} onChange={(date) => setTimesheetDate(date)} />
                            </Col>
                        </Form.Group>

                    </Form>
                </Container>

                <Container className="d-flex flex-column">
                    <Form>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextTimesheetHours">
                            <Form.Label column sm="2">{t('addHours')}:</Form.Label>
                            <Col sm="10">
                                <Form.Control value={hours} placeholder={"0"} onChange={timesheetChangeHandler}/>
                            </Col>
                        </Form.Group>

                    </Form>
                </Container>

                <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                    <Row>
                        <Col className="text-center">
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={addHoursToTimesheet}>{t('trainingCourseButton')}</Button>
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={resetForm}>{t('trainingCourseReset')}</Button>
                        </Col>
                    </Row>
                </Container>
            </Col>
        </Row>
    </Container>
        </Container>)

}

export default ProfilePage;
