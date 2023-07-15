import React, {useState} from "react";
import {Button, Col, Container, Form, Image, Row} from "react-bootstrap";
import axios from "axios";
import {useLocation, useNavigate} from "react-router-dom";
import Header from "../../layout/Header/Header";

/**
 * This is the page which allows the user to change their password assuming they known their old password. Otherwise,
 * they must request an admin user to reset their password.
 * @returns {JSX.Element} to be displayed to the user.
 */
function ChangePassword (props) {

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmedPassword, setConfirmedPassword] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    /**
     * Retrieve the current token either from the supplied state or empty if we are in doc mode.
     * @returns the current token as a string.
     */
    function getToken() {
        if ( props.docMode && props.docMode==='true') {
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
        if ( props.docMode && props.docMode==='true') {
            return "";
        } else {
            return location.state.company;
        }
    }

    /**
     * Set the current password that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function currentPasswordChangeHandler(event) {
        setCurrentPassword(event.target.value);
    }

    /**
     * Set the new password that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function newPasswordChangeHandler(event) {
        setNewPassword(event.target.value);
    }

    /**
     * Set the confirmed password that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function confirmedPasswordChangeHandler(event) {
        setConfirmedPassword(event.target.value)
    }

    /**
     * Method to reset form to initial values.
     */
    function resetForm () {
        setCurrentPassword("");
        setNewPassword("");
        setConfirmedPassword("");
    }

    /**
     *  Change the password by sending the request to the API assuming that the new and confirmed password
     *  are the same. Display an alert with either password changed successfully or error message.
     */
    function changePassword() {
        if ( newPassword !== confirmedPassword ) {
            alert('The two new passwords were not identical! Password could not be changed');
            return;
        }
        axios.patch(process.env.REACT_APP_SERVER_URL + '/user/password', {
            company: getCompany(),
            username: getUsername(),
            token: getToken(),
            currentPassword: currentPassword,
            newPassword: newPassword
        }).then(function (response) {
            if ( response.status === 200 ) {
                alert('Password was changed successfully! Please login again to continue');
                navigate("/")
            }
        }).catch(function (error) {
            alert('Password could not be changed. Please try again later.');
        });
    }

    /**
     * Get the username of the currently logged in user who we will display absences for.
     * @returns the username
     */
    function getUsername() {
        return getToken().split("-")[0];
    }

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Container fluid>
            <Header token={getToken()} company={getCompany()}/>
        <Container fluid className="p-3 my-5 h-custom">
        <Row className="d-flex flex-row align-items-center justify-content-center">
            <Col>
                <Image src="https://www.davelee.de/common/assets/img/portfolio/personalman-logo.png"
                       className="d-block mx-auto img-fluid w-50" alt="PersonalMan Logo"/>
            </Col>
            <Col>
                <Container className="d-flex flex-row align-items-center justify-content-center mb-3">
                    <Row>
                        <Col>
                            <h3>Change the password for user: {getUsername()}</h3>
                        </Col>
                    </Row>
                </Container>

                <Container className="d-flex flex-column">
                    <Form>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextCurrentPassword">
                            <Form.Label column sm="2">Current Password:</Form.Label>
                            <Col sm="10">
                                <Form.Control value={currentPassword} type="password" placeholder="Current Password" onChange={currentPasswordChangeHandler}/>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextNewPassword">
                            <Form.Label column sm="2">New Password:</Form.Label>
                            <Col sm="10">
                                <Form.Control value={newPassword} type="password" placeholder="New Password" onChange={newPasswordChangeHandler}/>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextConfirmPassword">
                            <Form.Label column sm="2">Confirm Password:</Form.Label>
                            <Col sm="10">
                                <Form.Control value={confirmedPassword} type="password" placeholder="Confirmed Password" onChange={confirmedPasswordChangeHandler}/>
                            </Col>
                        </Form.Group>

                    </Form>
                </Container>

                <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                    <Row>
                        <Col className="text-center">
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={changePassword}>Change Password</Button>
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={resetForm}>Reset</Button>
                        </Col>
                    </Row>
                </Container>
            </Col>
        </Row>
    </Container>
        </Container>)

}

export default ChangePassword;
