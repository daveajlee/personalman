import React from "react";
import {Col, Container, Image, Row} from "react-bootstrap";
import AddUserForm from "../components/AddUserForm";

function RegisterUser () {

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
                            <h3>Getting Started: Registering a new User</h3>
                        </Col>
                    </Row>
                </Container>

                <AddUserForm/>

            </Col>
        </Row>
    </Container>)

}

export default RegisterUser;
