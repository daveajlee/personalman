import {Modal} from "react-bootstrap";
import AddUserForm from "../../forms/AddUserForm/AddUserForm";
import PropTypes from "prop-types";
import {useTranslation} from "react-i18next";

type AddUserModalProps = {
    company: string;
    token: string;
    showAddUserModal: boolean;
    setShowAddUserModal: Function;
}

/**
 * This is the modal window to show the add user form. The modal is used for admin users who want to create new users
 * for their company.
 * @param props setShowAddUserModal - function to determine whether modal should be shown or not,
 * company - the company that users should be created for.
 * @returns {JSX.Element} to be displayed to the user.
 */
function AddUserModal ({company, showAddUserModal, setShowAddUserModal, token}: AddUserModalProps): React.JSX.Element {

    /**
     * Function to handle the case that the user clicks on the close button in the modal.
     */
    const handleAddUserClose = () => setShowAddUserModal(false);

    const {t} = useTranslation();

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Modal show={showAddUserModal} onHide={handleAddUserClose}>
            <Modal.Header closeButton>
                <Modal.Title>{t('addUserModalTitle')}</Modal.Title>
            </Modal.Header>
            <Modal.Body><AddUserForm companyName={company} token={token} handleAddUserClose={handleAddUserClose}/></Modal.Body>
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
