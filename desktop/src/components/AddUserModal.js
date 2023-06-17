import {Button, Modal} from "react-bootstrap";
import React from "react";
import AddUserForm from "./AddUserForm";

function AddUserModal (props) {

    const handleAddUserClose = () => props.setShowAddUserModal(false);

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
