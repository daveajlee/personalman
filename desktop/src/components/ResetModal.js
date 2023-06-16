import {Button, Col, Form, Modal, Row} from "react-bootstrap";
import React, {useState} from "react";
import axios from "axios";

function ResetModal (props) {

    const handleResetClose = () => props.setShowResetModal(false);
    const [newPassword, setNewPassword] = useState("");
    const [confirmedPassword, setConfirmedPassword] = useState("");

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
     * Reset the password for the supplied username and the passwords as long as the passwords are the same.
     */
    function resetPassword() {
        if ( newPassword !== confirmedPassword ) {
            alert('The two new passwords were not identical! Password could not be changed');
            return;
        }
        axios.patch('http://localhost:8150/api/user/reset', {
            company: props.company,
            username: props.username,
            token: props.token,
            password: newPassword
        }).then(function (response) {
            if ( response.status === 200 ) {
                alert('Password was reset successfully!');
                props.setShowResetModal(false);
            }
        }).catch(function (error) {
            alert('Password could not be changed. Please try again later.');
        });

    }

    return (
        <Modal show={props.showResetModal} onHide={handleResetClose}>
            <Modal.Header closeButton>
                <Modal.Title>Reset Password - {props.username}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
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
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={resetPassword}>
                    Reset Password
                </Button>
                <Button variant="primary" onClick={handleResetClose}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ResetModal;
