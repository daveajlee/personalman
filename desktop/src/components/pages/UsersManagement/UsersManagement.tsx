import {useLocation, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import Header from "../../layout/Header/Header";
import {Button, Col, Container, Row} from "react-bootstrap";
import axios from "axios";
import StatisticsModal from "../../modals/StatisticsModal/StatisticsModal";
import ResetModal from "../../modals/ResetModal/ResetModal";
import AddUserModal from "../../modals/AddUserModal/AddUserModal";
import {useTranslation} from "react-i18next";
import * as React from "react";

type UsersManagementProps = {
    docMode: string;
}

type User = {
    username: string;
    firstName: string;
    surname: string;
}

/**
 * This is the page which allows an admin user to manage users in their company.
 * @returns {JSX.Element} to be displayed to the user.
 */
function UsersManagement({docMode}: UsersManagementProps): React.JSX.Element {

    const location = useLocation();
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();
    const {t} = useTranslation();

    const [selectedUsername, setSelectedUsername] = useState('');
    const [showStatisticsModal, setShowStatisticsModal] = useState(false);
    const [showResetModal, setShowResetModal] = useState(false);
    const [showAddUserModal, setShowAddUserModal] = useState(false);

    /**
     * Retrieve the current token either from the supplied state or empty if we are in doc mode.
     */
    const token = (docMode && docMode==='true') ? "" : location.state.token;

    /**
     * Retrieve the company either from the supplied state or empty if we are in doc mode.
     */
    const company = ( docMode && docMode==='true') ? "" : location.state.company;

    /**
     * Retrieve the year either from the supplied state or empty if we are in doc mode.
     * @returns the current year as a number.
     */
    function getYear() {
        if ( docMode && docMode==='true') {
            return "";
        } else {
            return location.state.year ? location.state.year : new Date().getFullYear()
        }
    }

    /**
     * This function retrieves the current list of users which exists for the specified company assuming the access
     * token is valid.
     */
    useEffect(() => {
        axios.get(import.meta.env.REACT_APP_SERVER_URL + '/users/?company=' + company + '&token=' + token)
            .then(res => {
                const result = res.data;
                setUsers(result['userResponses']);
            }).catch(error => {
                console.error(error);
        })
    }, [token, company]);

    /**
     * This function deletes the user with the specified username assuming the user confirms that this is what they want to do.
     * @param username the username of the user to delete
     */
    function deleteUser(username: string) {
        if(window.confirm(t('usersManagementDeleteUser', { username: username }))) {
            axios.delete(import.meta.env.REACT_APP_SERVER_URL + '/user/?company=' + company + '&username=' + username +
                '&token=' + token)
                .then(function (response) {
                    if ( response.status === 200 ) {
                        alert(t('usersManagementDeleteUserSuccess'));
                        window.location.reload();
                    }
                }).catch(function (error) {
                    console.log(error); });
        }
    }

    /**
     * This function shows the statistics of the user with the specified username.
     * @param username the username that the user wants to view statistics for.
     */
    function statisticsUser(username: string) {
        setSelectedUsername(username);
        setShowStatisticsModal(true);
    }

    /**
     * This function enables a new password to be set for the user with the specified username.
     * @param username the username that the user wants to reset the password for.
     */
    function resetUser(username: string) {
        setSelectedUsername(username);
        setShowResetModal(true);
    }

    /**
     * This function enables a new user to be added.
     */
    function addUser() {
        setShowAddUserModal(true);
    }

    /**
     * This function shows all absences.
     */
    function allAbsences() {
        navigate('/allAbsences', {state:{token: token, month: (new Date().getMonth()+1), year: new Date().getFullYear(), company: company }});
    }

    /**
     * Display the relevant elements and data to the user.
     */
    // @ts-ignore
    return (
        <Container fluid>
            <Header token={token} company={company}/>

            <Container fluid className="p-3 my-5 h-custom">
                <Row className="d-flex flex-row align-items-center justify-content-center">
                    <Col>
                        <h1 className="text-center">{t('usersManagementTitle')} {company}</h1>
                    </Col>
                </Row>
            {users.map((d: User) => (<Row className="align-items-center justify-content-center mt-3" key={d.username}>

                <Col xs={12} sm={12} md={6} lg={8}>
                    <h4 className="text-center">{d.firstName} {d.surname} ({d.username}) </h4>
                </Col>
                <Col xs={12} sm={12} md={6} lg={4} className="align-items-center justify-content-center mt-3">
                    <Button className="me-2 align-items-center justify-content-center" variant="info" size='lg' onClick={statisticsUser.bind(null, d.username)}>{t('usersManagementStatisticsButton')}</Button>
                    <Button className="me-2 align-items-center justify-content-center" variant="warning" size='lg' onClick={resetUser.bind(null, d.username)}>{t('usersManagementResetButton')}</Button>
                    <Button className="me-2 align-items-center justify-content-center" variant="danger" size='lg' onClick={deleteUser.bind(null, d.username)}>{t('usersManagementDeleteButton')}</Button>
                </Col>
            </Row>))}
            </Container>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={addUser}>{t('usersManagementAddUserButton')}</Button>
                        <Button className="mb-0 px-5 me-2" size='lg' onClick={allAbsences}>{t('usersManagementAllAbsencesButton')}</Button>
                    </Col>
                </Row>
            </Container>

            <StatisticsModal year={ getYear() } company={company}
                             token={token} showStatisticsModal={showStatisticsModal} setShowStatisticsModal={setShowStatisticsModal}
                             username={selectedUsername}>
            </StatisticsModal>

            <ResetModal company={company} token={token} showResetModal={showResetModal}
                        setShowResetModal={setShowResetModal} username={selectedUsername}>
            </ResetModal>

            <AddUserModal company={company} token={token} showAddUserModal={showAddUserModal}
                        setShowAddUserModal={setShowAddUserModal}>
            </AddUserModal>

        </Container>

    );

}

export default UsersManagement;
