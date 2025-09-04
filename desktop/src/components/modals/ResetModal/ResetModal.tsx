import {Button, Col, Form, Modal, Row} from "react-bootstrap";
import {useState} from "react";
import axios from "axios";
import PropTypes from "prop-types";
import {useTranslation} from "react-i18next";
import * as React from "react";

type ResetModalProps = {
    setShowResetModal: Function;
    company: string;
    username: string;
    token: string;
    showResetModal: boolean;
}

/**
 * This is the modal window to show the reset password form. The modal is used for admin users who want to reset
 * the password for a particular user.
 * @param props setShowResetModal - function to determine whether modal should be shown or not,
 * company - the company that the user belongs to, username - the username of the user that the password should be reset for,
 * token - the current user access token of the admin user.
 * @returns {JSX.Element} to be displayed to the user.
 */
function ResetModal ({setShowResetModal, company, username, token, showResetModal}: ResetModalProps): React.JSX.Element {

    /**
     * Function to handle the case that the user clicks on the close button in the modal.
     */
    const handleResetClose = () => setShowResetModal(false);

    const [newPassword, setNewPassword] = useState("");
    const [confirmedPassword, setConfirmedPassword] = useState("");
    const {t} = useTranslation();

    /**
     * Set the new password that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function newPasswordChangeHandler(event: React.ChangeEvent<HTMLInputElement>) {
        setNewPassword(event.target.value);
    }

    /**
     * Set the confirmed password that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function confirmedPasswordChangeHandler(event: React.ChangeEvent<HTMLInputElement>) {
        setConfirmedPassword(event.target.value)
    }

    /**
     * Reset the password for the supplied username and the passwords as long as the passwords are the same.
     */
    function resetPassword() {
        if ( newPassword !== confirmedPassword ) {
            alert(t('resetPasswordNotIdentical'));
            return;
        }
        axios.patch(import.meta.env.VITE_SERVER_URL + '/user/reset', {
            company: company,
            username: username,
            token: token,
            password: newPassword
        }).then(function (response) {
            if ( response.status === 200 ) {
                alert(t('resetPasswordSuccess'));
                setShowResetModal(false);
            }
        }).catch(function (error) {
            alert(t('resetPasswordError: ' + error));
        });

    }

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Modal show={showResetModal} onHide={handleResetClose}>
            <Modal.Header closeButton>
                <Modal.Title>{t('resetPasswordTitle')} - {username}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextNewPassword">
                        <Form.Label column sm="2">{t('resetPasswordNewPassword')}:</Form.Label>
                        <Col sm="10">
                            <Form.Control value={newPassword} type="password" placeholder={t('resetPasswordNewPassword')} onChange={newPasswordChangeHandler}/>
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row} className="mb-3" controlId="formPlaintextConfirmPassword">
                        <Form.Label column sm="2">{t('resetPasswordConfirmPassword')}:</Form.Label>
                        <Col sm="10">
                            <Form.Control value={confirmedPassword} type="password" placeholder={t('resetPasswordConfirmPassword')} onChange={confirmedPasswordChangeHandler}/>
                        </Col>
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={resetPassword}>
                    {t('resetPasswordModalButton')}
                </Button>
                <Button variant="primary" onClick={handleResetClose}>
                    {t('resetPasswordCloseModalButton')}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ResetModal;

ResetModal.propTypes = {
    /**
     * the company that the user belongs to
     */
    company: PropTypes.string,

    /**
     * function to determine whether modal should be shown or not
     */
    setShowResetModal: PropTypes.func,

    /**
     * the username of the user that the password should be reset for
     */
    username: PropTypes.string,

    /**
     * the current user access token of the admin user
     */
    token: PropTypes.string,
}

ResetModal.defaultProps = {
    company: 'Required',
    username: 'Required',
    token: 'Required'
};
