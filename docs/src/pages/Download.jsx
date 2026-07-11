import React from "react";
import {Button, Card, Col, Container, Row} from "react-bootstrap";

const Download = () => (
    <Container fluid>
        <Row>
            <Col>
                <h2 className="mt-5 text-center">Download</h2>
            </Col>
        </Row>
        <Row className="mt-5">
            <Col xs={12} sm={12} md={6} lg={6}>
                <Card className="text-center nav-card">
                    <Card.Body>
                        <Card.Title>Server</Card.Title>
                        <Card.Text>
                            A running server instance is required before any client of PersonalMan can be used. This
                            server can be run on any computer which has Node.JS installed. It can be
                             manually started via the supplied Zip file. A docker image will be provided in the near future.
                             <br className="d-none d-lg-block"/><br className="d-none d-lg-block"/>
                        </Card.Text>
                        <Button variant="primary" href="">Download Server</Button>
                        <Button variant="primary" className="ms-3" disabled>Docker (Coming Soon)</Button>
                    </Card.Body>
                </Card>
            </Col>
            <Col xs={12} sm={12} md={6} lg={6}>
                <Card className="text-center nav-card">
                    <Card.Body>
                        <Card.Title>Desktop Client</Card.Title>
                        <Card.Text>
                            The PersonalMan Web & Desktop Client can be used in web browsers which support JavaScript
                            or natively on desktop operating systems such as Windows, Linux and Mac by utilising the
                            Electron framework.
                            <br className="d-none d-lg-block"/><br className="d-none d-lg-block"/>
                        </Card.Text>
                        <Button variant="primary"
                                href="https://github.com/daveajlee/personalman/releases">Download Client</Button>
                    </Card.Body>
                </Card>
            </Col>
        </Row>
    </Container>
);

export default Download;
