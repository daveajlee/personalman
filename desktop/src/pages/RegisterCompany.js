import React, {useState} from "react";
import {Button, Col, Container, Form, Image, Row} from "react-bootstrap";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function RegisterCompany () {

    const [name, setName] = useState("");
    const [annualLeave, setAnnualLeave] = useState(0);
    const [country, setCountry] = useState("Germany");
    const navigate = useNavigate();

    /**
     * Set the name that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function nameChangeHandler(event) {
        setName(event.target.value);
    }

    /**
     * Set the annual leave that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function annualLeaveChangeHandler(event) {
        setAnnualLeave(event.target.value);
    }

    /**
     * Set the country that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function countryChangeHandler(event) {
        setCountry(event.target.value)
    }

    /**
     * Register the new company by sending the request to the API. Display an alert and then move back to the home page.
     */
    function registerCompany() {
        axios.post('http://localhost:8150/api/company/', {
            name: name,
            defaultAnnualLeaveInDays: annualLeave,
            country: country
        }).then(function (response) {
            if ( response.status === 201 ) {
                alert('Thank you for registering for PersonalMan. You can now create users on the login page.')
                navigate("/")
            }
        }).catch(function (error) {
            console.log(error);
        });
    }

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
                            <h3>Getting Started: Registering a new Company/Organisation</h3>
                        </Col>
                    </Row>
                </Container>

                <Container className="d-flex flex-column">
                    <Form>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextName">
                            <Form.Label column sm="2">Name:</Form.Label>
                            <Col sm="10">
                                <Form.Control type="text" placeholder="Name" onChange={nameChangeHandler}/>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextEmail">
                            <Form.Label column sm="2">Default Annual Leave:</Form.Label>
                            <Col sm="10">
                                <Form.Control type="number" placeholder="A number between 1 and 365" min="1" max="365" onChange={annualLeaveChangeHandler}/>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextCompany">
                            <Form.Label column sm="2">Base Country:</Form.Label>
                            <Col sm="10">
                                <Form.Select aria-label="Company Name" onChange={countryChangeHandler}>
                                    <option key="de">Germany</option>
                                    <option key="gb-eng">England</option>
                                    <option key="gb-sct">Scotland</option>
                                </Form.Select>
                            </Col>
                        </Form.Group>

                    </Form>
                </Container>

                <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                    <Row>
                        <Col className="text-center">
                            <Button className="mb-0 px-5" size='lg' onClick={registerCompany}>Register</Button>
                        </Col>
                    </Row>
                </Container>
            </Col>
        </Row>
    </Container>)

}

export default RegisterCompany;
