import {useLocation} from "react-router-dom";
import React, {useEffect, useState} from "react";
import Header from "../components/Header";
import {Button, Col, Container, Row} from "react-bootstrap";
import axios from "axios";
import StatisticsModal from "../components/StatisticsModal";

function UsersManagement() {

    const location = useLocation();
    const [users, setUsers] = useState([]);

    const [selectedUsername, setSelectedUsername] = useState('');
    const [showStatisticsModal, setShowStatisticsModal] = useState(false);

    /**
     * This function retrieves the current list of users which exists for the specified company assuming the access
     * token is valid.
     */
    useEffect(() => {
        axios.get('http://localhost:8150/api/users/?company=' + location.state.company + '&token=' + location.state.token)
            .then(res => {
                const result = res.data;
                console.log(result['userResponses']);
                setUsers(result['userResponses']);
            })
    }, [location.state.token, location.state.company]);

    /**
     * This function deletes the user with the specified username assuming the user confirms that this is what they want to do.
     * @param username the username of the user to delete
     */
    function deleteUser(username) {
        if(window.confirm('Do you really want to delete the user - ' + username + '?')) {
            axios.delete('http://localhost:8150/api/user/?company=' + location.state.company + '&username=' + username +
                '&token=' + location.state.token)
                .then(function (response) {
                    if ( response.status === 200 ) {
                        alert('User was deleted successfully!');
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
    function statisticsUser(username) {
        setSelectedUsername(username);
        setShowStatisticsModal(true);
    }

    return (
        <Container fluid>
            <Header token={location.state.token} company={location.state.company}/>

            <Container fluid className="p-3 my-5 h-custom">
                <Row className="d-flex flex-row align-items-center justify-content-center">
                    <Col>
                        <h1 className="text-center">Users for company: {location.state.company}</h1>
                    </Col>
                </Row>
            {users.map(d => (<Row className="align-items-center justify-content-center mt-3" key={d.username}>

                <Col xs={12} sm={12} md={6} lg={8}>
                    <h4 className="text-center">{d.firstName} {d.surname} ({d.username}) </h4>
                </Col>
                <Col xs={12} sm={12} md={6} lg={4} className="align-items-center justify-content-center mt-3">
                    <Button className="me-2 align-items-center justify-content-center" variant="info" size='lg' onClick={statisticsUser.bind(this, d.username)}>Statistics</Button>
                    <Button className="me-2 align-items-center justify-content-center" variant="warning" size='lg'>Reset</Button>
                    <Button className="me-2 align-items-center justify-content-center" variant="danger" size='lg' onClick={deleteUser.bind(this, d.username)}>Delete</Button>
                </Col>
            </Row>))}
            </Container>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2" size='lg'>Add User</Button>
                        <Button className="mb-0 px-5 me-2" size='lg'>All Absences</Button>
                    </Col>
                </Row>
            </Container>

            <StatisticsModal year={ location.state.year ? location.state.year : new Date().getFullYear()} company={location.state.company}
                             token={location.state.token} showStatisticsModal={showStatisticsModal} setShowStatisticsModal={setShowStatisticsModal}
                             username={selectedUsername}>
            </StatisticsModal>

        </Container>

    );

}

export default UsersManagement;
