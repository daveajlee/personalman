import {Modal} from "react-bootstrap";
import React from "react";
import AddUserForm from "../../forms/AddUserForm/AddUserForm";
import PropTypes from "prop-types";

/**
 * This is the modal window to show the add user form. The modal is used for admin users who want to create new users
 * for their company.
 * @param props setShowAddUserModal - function to determine whether modal should be shown or not,
 * company - the company that users should be created for.
 * @returns {JSX.Element} to be displayed to the user.
 */
function AddUserModal (props) {

    /**
     * Function to handle the case that the user clicks on the close button in the modal.
     */
    const handleAddUserClose = () => props.setShowAddUserModal(false);

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Modal show={props.showAddUserModal} onHide={handleAddUserClose}>
            <Modal.Header closeButton>
                <Modal.Title>Add User</Modal.Title>
            </Modal.Header>
            <Modal.Body><AddUserForm company={props.company} handleAddUserClose={handleAddUserClose}/></Modal.Body>
        </Modal>
    );

}

export default AddUserModal;

AddUserModal.propTypes = {
    /**
     * the company that users should be created for.
     */
    company: PropTypes.string,

    /**
     * function to determine whether modal should be shown or not
     */
    setShowAddUserModal: PropTypes.func
}

AddUserModal.defaultProps = {
    company: 'Required'
};
