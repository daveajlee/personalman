import React from "react";
import logo from "../personalman-logo.png";
import visionImage from "../joshua-earle-Dwheufds6kQ-unsplash.jpg";
import roadmapImage from "../jess-bailey-mexeVPlTB6k-unsplash.jpg";
import architectureImage from "../arpad-czapp-wS250FJl5Uw-unsplash.jpg";
import apiImage from "../douglas-lopes-ehyV_XOZ4iA-unsplash.jpg";
import {Button, Card, Col, Container, Row} from "react-bootstrap";
import {Link} from "react-router-dom";

const Home = () => (
    <Container fluid>
        <Row>
            <Col className="text-center"><img src={logo} alt="PersonalMan"
                      className="img-responsive"/></Col>
        </Row>
        <Row>
            <Col className="text-center mt-5"><h1>PersonalMan - HR Management Software</h1></Col>
        </Row>
        <Row>
            <Col className="text-left mt-3"><p>PersonalMan is a open source employee management software for small mainly non-profit organisations.
                The software helps these organisations to record employee information and provide absence management functionality thereby
                lowering the administration overhead. This website provides information about the vision and roadmap
                PersonalMan into their own systems through the flexible client server API architecture.</p></Col>
        </Row>
        <Row>
            <Col className="text-left mt-3"><p>If you simply want to try out PersonalMan without reading the complete documentation first
                (not recommended) then you can simply download the latest version: </p><p className="text-center mt-2"><Button as={Link} to="/download">Download</Button></p></Col>
        </Row>
        <hr/>
        <Row className="mt-3">
            <Col>
                <Card style={{ width: '18rem' }}>
                    <Card.Img variant="top" src={visionImage} />
                    <Card.Body>
                        <Card.Title>Vision</Card.Title>
                        <Card.Text>
                            Find out more about the vision and goals behind PersonalMan.
                        </Card.Text>
                        <Button variant="primary" as={Link} to="/vision">Vision</Button>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card style={{ width: '18rem' }}>
                    <Card.Img variant="top" src={roadmapImage} />
                    <Card.Body>
                        <Card.Title>Features & Roadmap</Card.Title>
                        <Card.Text>
                            Current features and the next features that are planned.
                        </Card.Text>
                        <Button variant="primary" as={Link} to="/features">Features</Button>
                        <Button variant="primary" className="ms-3" as={Link} to="/roadmap">Roadmap</Button>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card style={{ width: '18rem' }}>
                    <Card.Img variant="top" src={architectureImage} />
                    <Card.Body>
                        <Card.Title>Architecture</Card.Title>
                        <Card.Text>
                            Documentation of the architecture design of PersonalMan.
                        </Card.Text>
                        <Button variant="primary" as={Link} to="/architecture">Architecture</Button>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card style={{ width: '18rem' }}>
                    <Card.Img variant="top" src={apiImage} />
                    <Card.Body>
                        <Card.Title>API</Card.Title>
                        <Card.Text>
                            Technical documentation for developers.
                        </Card.Text>
                        <Button variant="primary" as={Link} to="/api">API</Button>
                    </Card.Body>
                </Card>
            </Col>
        </Row>
    </Container>
);

export default Home;
