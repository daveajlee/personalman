import {type ChangeEvent, useEffect, useState} from "react";
import {Container, Col, Row, Form, Button, Image} from "react-bootstrap";
import axios from 'axios';
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import logo from './../../../assets/personalman-logo.png';
import * as React from "react";

/**
 * This component represents the login screen to allow users to login to PersonalMan.
 */
function Login(): React.JSX.Element {

    const [companies, setCompanies] = useState([]);
    const [company, setCompany] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const {t} = useTranslation();

    /**
     * Load the list of available companies via the REST API.
     */
    useEffect(() => {
        console.log(import.meta.env.VITE_SERVER_URL);
        axios.get(import.meta.env.VITE_SERVER_URL + `/companies/`)
            .then(res => {
                console.log(res);
                const companies = res.data;
                setCompanies(companies);
                setCompany(companies[0]);
            }).catch(error => {
                console.error(error);
        })
    }, []);

    /**
     * Set the company that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function handleCompanyChange(event: ChangeEvent<HTMLSelectElement>) {
        setCompany(event.target.value);
    }

    /**
     * Set the username that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function handleUsernameChange(event: ChangeEvent<HTMLInputElement>) {
        setUsername(event.target.value);
    }

    /**
     * Set the password that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function handlePasswordChange(event: ChangeEvent<HTMLInputElement>) {
        setPassword(event.target.value)
    }

    /**
     * Attempt to sign in the user with the supplied data. If it works, move to the absence management page. Otherwise,
     * display a simple alert.
     */
    function login() {
        axios.post(import.meta.env.REACT_APP_SERVER_URL + '/user/login', {
            company: company,
            username: username,
            password: password,
        }).then(function (response) {
            if ( response.status === 200 ) {
                navigate("/absences", {state:{token: response.data.token, company: company}})
            }
        }).catch(function (error) {
            alert('Please verify that the username, password and company are correct and try again.');
            console.log(error);
        });
    }

    /**
     * Method to reset form to initial values.
     */
    function resetForm () {
        setCompany(companies[0]);
        setUsername("");
        setPassword("");
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
                            <h1>{t('loginTitle')}</h1>
                        </Col>
                    </Row>
                </Container>

                <Container className="d-flex flex-column">
                    <Form>
                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextCompany">
                            <Form.Label column sm="2">{t('loginCompanyLabel')}:</Form.Label>
                            <Col sm="10">
                                <Form.Select value={company} aria-label="Company Name" onChange={handleCompanyChange}>
                                    {companies.map(company => ( <option key={company}>{company}</option>))}
                                </Form.Select>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextUsername">
                            <Form.Label column sm="2">{t('loginUsernameLabel')}:</Form.Label>
                            <Col sm="10">
                                <Form.Control type="text" value={username} placeholder={t('loginUsernameLabel')} autoComplete="username" onChange={handleUsernameChange}/>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                            <Form.Label column sm="2">{t('loginPasswordLabel')}</Form.Label>
                            <Col sm="10">
                                <Form.Control type="password" value={password} placeholder={t('loginPasswordLabel')} autoComplete="current-password" onChange={handlePasswordChange}/>
                            </Col>
                        </Form.Group>
                    </Form>
                </Container>

                <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                    <Row>
                        <Col className="text-center">
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={login}>{t('loginLoginButton')}</Button>
                            <Button className="mb-0 px-5 me-2" size='lg' onClick={resetForm}>{t('loginResetButton')}</Button>
                            <p className="small fw-bold mt-2 pt-1 mb-2">{t('loginRegisterUserTeaser')} <a href="/#/registerUser"
                                                                                                  className="link-danger">{t('loginRegisterUserLink')}</a>
                            </p>
                            <p className="small fw-bold mt-2 pt-1 mb-2">{t('loginRegisterCompanyTeaser')} <a href="/#/registerCompany"
                                                                                                               className="link-danger">{t('loginRegisterCompanyLink')}</a>
                            </p>
                        </Col>
                    </Row>
                </Container>
            </Col>
        </Row>
    </Container>)
}

export default Login;
