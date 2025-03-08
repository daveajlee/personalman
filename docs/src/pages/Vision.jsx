import React from "react";
import {Accordion, Card, Col, Container, Row} from "react-bootstrap";
import visionImage from "../joshua-earle-Dwheufds6kQ-unsplash.jpg";

const Vision = () => (
    <Container fluid>
        <Row>
            <Col>
                <Card className="border-0">
                    <Card.Img variant="top" className="img-responsive w-25 rounded mx-auto d-block mt-3" src={visionImage} />
                    <Card.Title as="h3" className="mt-5 text-center">PersonalMan Vision</Card.Title>
                    <Card.Body className="text-center mb-5">PersonalMan enables organisations to
                        automate their employee data workflows with minimal IT knowledge.</Card.Body>
                </Card>
            </Col>
        </Row>
        <Row>
            <Col>
                <h3 className="mt-5 text-center">Goals</h3>
                <Accordion className="mt-3">
                    <Accordion.Item eventKey="0">
                        <Accordion.Header>Easy to use</Accordion.Header>
                        <Accordion.Body>
                            PersonalMan should only require basic computer knowledge. Any person who is capable of using
                            a device connected to the internet (e.g. laptop, PC or smartphone) should be able to use PersonalMan without further training.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="1">
                        <Accordion.Header>High degree of process automation</Accordion.Header>
                        <Accordion.Body>
                            PersonalMan should automate processes such as absence management in such
                            a way that no manual effort is required from any person in the organisation. This does not include
                            manual approval steps which the organisation explicitly wants to have.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="2">
                        <Accordion.Header>Suitable for hybrid working</Accordion.Header>
                        <Accordion.Body>
                            The Corona pandemic has shown the value of hybrid working for all types
                            of organisations. Small organisations often did hybrid working even before the pandemic. Therefore the design
                            of PersonalMan should actively encourage hybrid working so that it can be used wherever the user is as long as
                            they have a laptop or smartphone device with them.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="3">
                        <Accordion.Header>High performance and reliability</Accordion.Header>
                        <Accordion.Body>
                            PersonalMan should provide small organisations with the performance
                            and reliability that they need. Users should get quick feedback whenever they make a request and no data
                            should be intentionally lost.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="4">
                        <Accordion.Header>User Permissions</Accordion.Header>
                        <Accordion.Body>
                            PersonalMan should provide the relevant features for a particular user in the context of their position
                            in the organisation. For example, a manager should be able to approve absences whereas employees
                            can only apply to be absent.
                        </Accordion.Body>
                    </Accordion.Item>
                </Accordion>
            </Col>
        </Row>
    </Container>
);

export default Vision;
