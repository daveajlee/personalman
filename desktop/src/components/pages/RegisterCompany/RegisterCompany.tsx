import {type ChangeEvent, useState} from "react";
import {Button, Col, Container, Form, Image, Row} from "react-bootstrap";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import {useTranslation} from "react-i18next";
import logo from './../../../assets/personalman-logo.png';
import * as React from "react";

/**
 * This is the page which allows the user to register a new company in PersonalMan.
 * @returns {JSX.Element} to be displayed to the user.
 */
function RegisterCompany (): React.JSX.Element {

    const [name, setName] = useState("");
    const [annualLeave, setAnnualLeave] = useState(20);
    const [country, setCountry] = useState("Germany");
    const navigate = useNavigate();
    const [errorText, setErrorText] = useState("");

    const {t} = useTranslation();

    /**
     * Set the name that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function nameChangeHandler(event: ChangeEvent<HTMLInputElement>) {
        setName(event.target.value);
    }

    /**
     * Set the annual leave that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function annualLeaveChangeHandler(event: ChangeEvent<HTMLInputElement>) {
        setAnnualLeave(parseInt(event.target.value));
    }

    /**
     * Set the country that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function countryChangeHandler(event: ChangeEvent<HTMLSelectElement>) {
        setCountry(event.target.value)
    }

    /**
     * Method to reset form to initial values.
     */
    function resetForm () {
        setName("");
        setAnnualLeave(20);
        setCountry("Germany")
    }

    /**
     * Register the new company by sending the request to the API. Display an alert and then move back to the home page.
     */
    function registerCompany() {
        console.log('*****');
        const userAgent = navigator.userAgent.toLowerCase();
        console.log(userAgent);
        axios.post(import.meta.env.REACT_APP_SERVER_URL  + '/company/', {
            name: name,
            defaultAnnualLeaveInDays: annualLeave,
            country: country
        }).then(function (response) {
            if ( response.status === 201 ) {
                alert('Thank you for registering for PersonalMan. You can now create users on the login page.')
                navigate("/registerUser")
            } else {
                setErrorText('Server is not available. Please check the server is configured correctly.')
            }
        }).catch(function (error) {
            setErrorText('Server is not available. Please check the server is configured correctly.')
            console.log(error);
        });
    }

    /**
     * Display the relevant elements and data to the user.
     */
    return (<Container fluid className="p-3 my-5 h-custom">
        <Row className="d-flex flex-row align-items-center justify-content-center">
            <Col>
                <Image src={logo}
                       className="d-block mx-auto img-fluid w-50" alt="PersonalMan Logo"/>
            </Col>
            <Col>
                <Container className="d-flex flex-row align-items-center justify-content-center mb-3">
                    <Row>
                        <Col>
                            <h3>Test {t('registerCompanyTitle')}</h3>
                        </Col>
                    </Row>
                </Container>

                <Container className="d-flex flex-column">
                    <Form>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextName">
                            <Form.Label column sm="2">{t('registerCompanyNameLabel')}:</Form.Label>
                            <Col sm="10">
                                <Form.Control value={name} type="text" placeholder={t('registerCompanyNameLabel')} onChange={nameChangeHandler}/>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextEmail">
                            <Form.Label column sm="2">{t('registerCompanyAnnualLeaveLabel')}:</Form.Label>
                            <Col sm="10">
                                <Form.Control type="number" value={annualLeave} min="20" max="40" onChange={annualLeaveChangeHandler}/>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextCompany">
                            <Form.Label column sm="2">{t('registerCompanyBaseCountryLabel')}:</Form.Label>
                            <Col sm="10">
                                <Form.Select value={country} aria-label="Company Name" onChange={countryChangeHandler}>
                                    <option key="de">{t('registerCompanyCountryGermany')}</option>
                                    <option key="gb-eng">{t('registerCompanyCountryEngland')}</option>
                                    <option key="gb-sct">{t('registerCompanyCountryScotland')}</option>
                                </Form.Select>
                            </Col>
                        </Form.Group>

                    </Form>
                </Container>

                <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                    <Row>
                        <Col className="text-center">
                            <h2 style={{color:`red`}}>{errorText}</h2>
                        </Col>
                    </Row>
                </Container>

                <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                    <Row>
                        <Col className="text-center">
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={registerCompany}>{t('registerCompanyRegisterButton')}</Button>
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={resetForm}>{t('registerCompanyResetButton')}</Button>
                        </Col>
                    </Row>
                </Container>
            </Col>
        </Row>
    </Container>)

}

export default RegisterCompany;
