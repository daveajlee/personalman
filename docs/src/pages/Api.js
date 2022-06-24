import React from "react";
import SwaggerUI from "swagger-ui-react"
import "swagger-ui-react/swagger-ui.css"
import {Card, Col, Container, Row} from "react-bootstrap";
import apiImage from "../douglas-lopes-ehyV_XOZ4iA-unsplash.jpg";

const Api = () => (
    <Container fluid>
        <Row>
            <Col>
                <Card>
                    <Card.Img variant="top" className="img-responsive w-25 rounded mx-auto d-block mt-3" src={apiImage} />
                    <Card.Title className="mt-5 text-center">API</Card.Title>
                    <Card.Body className="text-center mb-5">The current API of PersonalMan is displayed below. Please
                    note that this API should only be used as reference documentation. The functionality to
                    try out the API is not available on this page. If you would like to test the API or test a
                    client you have built for the API then you need to download PersonalMan Server from the Download
                    page.</Card.Body>
                </Card>
            </Col>
        </Row>
        <Row>
            <SwaggerUI url="swagger.json" />
        </Row>
    </Container>
);

export default Api;
