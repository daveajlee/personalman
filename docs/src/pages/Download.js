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
            <Col xs={12} sm={12} md={6} lg={4}>
                <Card className="text-center nav-card">
                    <Card.Body>
                        <Card.Title>Server</Card.Title>
                        <Card.Text>
                            A running server instance is required before any client of PersonalMan can be used. This
                            server can be run on any computer which supports Java 11 or above. It can be
                             manually started via the supplied JAR file or the supplied Docker image.
                             <br className="d-none d-lg-block"/><br className="d-none d-lg-block"/>
                        </Card.Text>
                        <Button variant="primary" href="https://github.com/daveajlee/personalman/packages/119518">JAR</Button>
                        <Button variant="primary" className="ms-3" href="https://hub.docker.com/r/daveajlee/personalman">Docker</Button>
                    </Card.Body>
                </Card>
            </Col>
            <Col xs={12} sm={12} md={6} lg={4}>
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
            <Col xs={12} sm={12} md={6} lg={4}>
                <Card className="text-center nav-card">
                    <Card.Body>
                        <Card.Title>Java API Implementation</Card.Title>
                        <Card.Text>
                            It is possible to write a separate client for PersonalMan by implementing the API described
                            on the API page. A Java Implementation of this API is provided via Maven which can then
                            be used as a dependency in other Java projects.
                            <br className="d-none d-lg-block"/><br className="d-none d-lg-block"/>
                        </Card.Text>
                        <Button variant="primary" href="https://search.maven.org/artifact/de.davelee.personalman/personalman-java-api">Maven Dependency</Button>
                    </Card.Body>
                </Card>
            </Col>
        </Row>
    </Container>
);

export default Download;
