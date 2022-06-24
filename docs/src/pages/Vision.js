import React from "react";
import {Card, Col, Container, Row, Tab, Tabs} from "react-bootstrap";
import visionImage from "../joshua-earle-Dwheufds6kQ-unsplash.jpg";

const Vision = () => (
    <Container fluid>
        <Row>
            <Col>
                <Card>
                    <Card.Img variant="top" className="img-responsive w-25 rounded mx-auto d-block mt-3" src={visionImage} />
                    <Card.Title className="mt-5 text-center">PersonalMan Vision</Card.Title>
                    <Card.Body className="text-center mb-5">PersonalMan enables organisations to
                        automate their employee data workflows with minimal IT knowledge.</Card.Body>
                </Card>
            </Col>
        </Row>
        <Row>
            <Col>
                <h5 className="mt-5 text-center">Goals</h5>
                <Tabs defaultActiveKey="profile" id="uncontrolled-tab-example" className="mb-3">
                    <Tab eventKey="easy" title="Easy to use">
                        <EasyToUse />
                    </Tab>
                    <Tab eventKey="automation" title="High degree of process automation">
                        <ProcessAutomation />
                    </Tab>
                    <Tab eventKey="hybrid" title="Suitable for hybrid working">
                        <HybridWorking />
                    </Tab>
                    <Tab eventKey="performance" title="High performance and reliability">
                        <Performance />
                    </Tab>
                    <Tab eventKey="opensource" title="Open source for everyone">
                        <OpenSource />
                    </Tab>
                </Tabs>
            </Col>
        </Row>
    </Container>
);

function EasyToUse() {
    return (<p className="text-center">Since the vision of PersonalMan is to enable organisations which have very
    little IT knowledge, all parts of PersonalMan from the user interface through to the technical documentation should
    be easy to use and require only an average knowledge of basic computing skills.</p>);
}

function ProcessAutomation() {
    return (<p className="text-center">PersonalMan should automate processes such as absence management in such
    a way that no manual effort is required from any person in the organisation. This does not include
    manual approval steps which the organisation explicitly wants to have.</p>);
}

function HybridWorking() {
    return (<p className="text-center">The Corona pandemic has shown the value of hybrid working for all types
    of organisations. Small organisations often did hybrid working even before the pandemic. Therefore the design
    of PersonalMan should actively encourage hybrid working so that it can be used wherever the user is as long as
        they have a laptop or smartphone device with them.</p>);
}

function Performance() {
    return (<p className="text-center">PersonalMan should provide small organisations with the performance
    and reliability that they need. Users should get quick feedback whenever they make a request and no data
    should be intentionally lost.</p>);
}

function OpenSource() {
    return (<p className="text-center">PersonalMan should be open source so that anyone can extend the feature set at any time.</p>);
}

export default Vision;
