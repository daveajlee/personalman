import React from "react";
import {Accordion, Col, Container, Row} from "react-bootstrap";
import architectureFigure from "../architecture.png";
import architectureSmall from "../architecture-mobile.png";
import MediaQuery from 'react-responsive';

const Architecture = () => (
    <Container fluid>
        <Row>
            <Col>
                <h2 className="mt-5 text-center">Architecture</h2>
                <MediaQuery query="(max-device-width: 900px)">
                    <Col className="text-center mt-5"><img src={architectureSmall} alt="PersonalMan"
                                                           className="img-responsive"/></Col>
                </MediaQuery>
                <MediaQuery query="(min-device-width: 901px)">
                    <Col className="text-center mt-5"><img src={architectureFigure} alt="PersonalMan"
                                                           className="img-responsive"/></Col>
                </MediaQuery>
            </Col>
        </Row>
        <Row>
            <Col>
                <p className="mt-5">The current architecture of PersonalMan is based on the classical client server architecture
                pattern. This pattern ensures a separation of concern between the client and the server. The client can focus
                on presenting the data effectively to the user and providing options of what the user may want to do next
                and the server can concentrate on processing the requests that the user has made and providing answers
                to the client. The communication between the client and server is managed through an API. A description
                of each part of the architecture diagram follows in the next section.</p>
            </Col>
        </Row>
        <Row>
            <Col>
                <Accordion defaultActiveKey="0">
                    <Accordion.Item eventKey="0">
                        <Accordion.Header>Client (PersonalMan Desktop Client)</Accordion.Header>
                        <Accordion.Body>
                            The current architecture of PersonalMan allows multiple clients to be built - each client
                            simply has to implement the API provided by the server. Currently there is only one client
                            which is available for download: the PersonalMan Desktop Java Client. This client implements
                            the API in Java and therefore is typically restricted to Desktop or Laptop systems. A responsive
                            browser-based client or a mobile app is possible in the current architecture and is part of
                            the roadmap of PersonalMan development.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="1">
                        <Accordion.Header>Load Balancing (Eureka)</Accordion.Header>
                        <Accordion.Body>
                            Whilst not an absolute requirement for a client server architecture, load balancing and
                            service registries is an important topic for this architecture pattern. PersonalMan
                            integrates Eureka per default but other load balancer systems can also be used if an
                            organisation needs to run multiple PersonalMan instances using an alternative technology.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="2">
                        <Accordion.Header>Server (PersonalMan Server)</Accordion.Header>
                        <Accordion.Body>
                            The server component is basically the brain behind PersonalMan in the current architecture.
                            The server is responsible for implementing the feature set and responding to all of the
                            requests that are sent by any client. The server is stateless and implements a REST API.
                            This ensures a simple and easy-to-use API which clients can then implement. The Server
                            component is currently built in Java using Spring Boot. The server component can optionally
                            also be deployed via Docker container(s).
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="3">
                        <Accordion.Header>Database (MongoDB)</Accordion.Header>
                        <Accordion.Body>
                            PersonalMan uses a database to save data about employees and absences. This data can
                            theoretically be stored in any type of database with minimal additional effort. In the
                            current implementation, MongoDB as a NoSQL database is used to ensure a quick answer to all
                            of the requests from the server. In order to safeguard consistency, only the server is allowed
                            to communicate with the database.
                        </Accordion.Body>
                    </Accordion.Item>
                </Accordion>
            </Col>
        </Row>
    </Container>
);

export default Architecture;
