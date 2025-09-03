import {Col, Container, Image, Row} from "react-bootstrap";
import logo from './../../../assets/personalman-logo.png';
import * as React from "react";

/**
 * This page is the error page which is displayed whenever the user visits an invalid url.
 * @returns {JSX.Element} to be displayed to the user.
 */
function ErrorPage(): React.JSX.Element {

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Container fluid className="p-3 my-5 h-custom">
            <Row className="d-flex flex-row align-items-center justify-content-center">
                <Col>
                    <Image src={logo}
                           className="d-block mx-auto img-fluid w-50" alt="PersonalMan Logo"/>
                </Col>
                <Col>
                    <Container className="d-flex flex-row align-items-center justify-content-center mb-3">
                        <Row>
                            <Col>
                                <h1>Sorry this page could not be found!</h1>

                                <h2><a href={"/"}>Click here</a> to return to the home page.</h2>
                            </Col>
                        </Row>
                    </Container>
                </Col>
            </Row>
        </Container>
    );

}

export default ErrorPage;
