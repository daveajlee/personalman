import React, {useEffect, useState} from "react";
import {Container, Col, Row, Form, Button, Image} from "react-bootstrap";
import axios from 'axios';

function Login() {

    const [companies, setCompanies] = useState([]);

    useEffect(() => {
        axios.get(`http://localhost:8150/api/companies/`)
            .then(res => {
                const companies = res.data;
                setCompanies(companies);
            })
    }, []);

    return (<Container fluid className="p-3 my-5 h-custom">
        <Row className="d-flex flex-row align-items-center justify-content-center">
            <Col>
                <Image src="https://www.davelee.de/common/assets/img/portfolio/personalman-logo.png"
                       className="d-block mx-auto img-fluid w-50" alt="PersonalMan Logo"/>
            </Col>
            <Col>
                <Container className="d-flex flex-row align-items-center justify-content-center mb-3">
                    <Row>
                        <Col>
                            <h1>Welcome to PersonalMan!</h1>
                        </Col>
                    </Row>
                </Container>

                <Container className="d-flex flex-column">
                    <Form>
                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextCompany">
                            <Form.Label column sm="2">Company:</Form.Label>
                            <Col sm="10">
                                <Form.Select aria-label="Company Name">
                                    {companies.map(company => ( <option key={company}>{company}</option>))}
                                </Form.Select>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextEmail">
                            <Form.Label column sm="2">Email:</Form.Label>
                            <Col sm="10">
                                <Form.Control type="text" placeholder="Email Address" autoComplete="email"/>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                            <Form.Label column sm="2">Password:</Form.Label>
                            <Col sm="10">
                                <Form.Control type="password" placeholder="Password" autoComplete="current-password"/>
                            </Col>
                        </Form.Group>
                    </Form>
                </Container>

                <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                    <Row>
                        <Col className="text-center">
                            <Button className="mb-0 px-5" size='lg'>Login</Button>
                            <p className="small fw-bold mt-2 pt-1 mb-2">Don't have an account? <a href="#!"
                                                                                                  className="link-danger">Register</a>
                            </p>
                        </Col>
                    </Row>
                </Container>
            </Col>
        </Row>
    </Container>)
}

export default Login;
