import React from "react";
import {Container, Col, Row, Form, Button, Image} from "react-bootstrap";

const Login = () => (

    <Container fluid className="p-3 my-5 h-custom">
        <Row className="d-flex flex-row align-items-center justify-content-center">
        <Col>
            <Image src="https://www.davelee.de/common/assets/img/portfolio/personalman-logo.png"
                     className="d-block mx-auto img-fluid w-50" alt="PersonalMan Logo"/>
        </Col>
            <Col>
                <Container className="d-flex flex-row align-items-center justify-content-center">
                    <Row>
                        <Col>
                            <p className="lead fw-normal mb-0 me-3">Welcome to PersonalMan! Please sign in...</p>
                        </Col>
                    </Row>
                </Container>

                <Container>
                    <Row>
                        <Col>
                            <Form.Select aria-label="Company Name">
                                <option>Select your company</option>
                            </Form.Select>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Form.Label htmlFor="inputEmail5">Email</Form.Label>
                            <Form.Control
                                type="text"
                                id="inputEmail5"
                                aria-describedby="emailHelpBlock"
                            />
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Form.Label htmlFor="inputPassword5">Password</Form.Label>
                            <Form.Control
                                type="password"
                                id="inputPassword5"
                                aria-describedby="passwordHelpBlock"
                            />
                        </Col>
                    </Row>
                </Container>

                <Container className="d-flex justify-content-between mb-4">
                    <Row>
                        <Col>
                            <Form.Check name='flexCheck' value='' id='flexCheckDefault' label='Remember me' />
                            <a href="!#">Forgot password?</a>
                        </Col>
                    </Row>
                </Container>

                <Container className='text-center text-md-start mt-4 pt-2'>
                    <Row>
                        <Col>
                            <Button className="mb-0 px-5" size='lg'>Login</Button>
                            <p className="small fw-bold mt-2 pt-1 mb-2">Don't have an account? <a href="#!" className="link-danger">Register</a></p>
                        </Col>
                    </Row>
                </Container>
            </Col>
        </Row>
    </Container>
);

export default Login;
