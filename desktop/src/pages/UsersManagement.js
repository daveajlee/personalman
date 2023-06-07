import {useLocation} from "react-router-dom";
import React, {useEffect, useState} from "react";
import Header from "../components/Header";
import {Button, Col, Container, Row} from "react-bootstrap";
import axios from "axios";

function UsersManagement() {

    const location = useLocation();
    const [users, setUsers] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8150/api/users/?company=' + location.state.company + '&token=' + location.state.token)
            .then(res => {
                const result = res.data;
                console.log(result['userResponses']);
                setUsers(result['userResponses']);
            })
    }, [location.state.token, location.state.company]);

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
                    <Button className="me-2 align-items-center justify-content-center" variant="info" size='lg'>Absences</Button>
                    <Button className="me-2 align-items-center justify-content-center" variant="warning" size='lg'>Reset</Button>
                    <Button className="me-2 align-items-center justify-content-center" variant="danger" size='lg'>Delete</Button>
                </Col>
            </Row>))}
            </Container>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2" size='lg'>Add User</Button>
                    </Col>
                </Row>
            </Container>

        </Container>
    );

}

export default UsersManagement;
