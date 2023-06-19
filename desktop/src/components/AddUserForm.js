import {Button, Col, Container, Form, Row} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

/**
 * This is the form to display when the user wishes to create a new user for a particular company.
 * @param props company - create a new user for this particular company name (optional).
 * @returns {JSX.Element} to be displayed to the user.
 */
function AddUserForm (props) {

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [company, setCompany] = useState("");
    const [position, setPosition] = useState("");
    const [workingDays, setWorkingDays] = useState([]);
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState("");
    const [startDate, setStartDate] = useState("");
    const [role, setRole] = useState("Employee");
    const navigate = useNavigate();

    const [companies, setCompanies] = useState([]);

    /**
     * Load the list of companies that are currently available in PersonalMan if no company name was supplied
     * in the parameters.
     */
    useEffect(() => {
        if ( props.company === '' ) {
            axios.get(`http://localhost:8150/api/companies/`)
                .then(res => {
                    const companies = res.data;
                    setCompanies(companies);
                    setCompany(companies[0]);
                })
        } else {
            setCompanies(new Array(props.company));
            setCompany(props.company);
        }
    }, [props.company]);

    /**
     * Set the first name that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function firstNameChangeHandler(event) {
        setFirstName(event.target.value);
    }

    /**
     * Set the last name that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function lastNameChangeHandler(event) {
        setLastName(event.target.value);
    }

    /**
     * Set the company that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function companyChangeHandler(event) {
        setCompany(event.target.value);
    }

    /**
     * Set the position that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function positionChangeHandler(event) {
        setPosition(event.target.value);
    }

    /**
     * Set the role that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function roleChangeHandler(event) {
        setRole(event.target.value);
    }

    /**
     * Set the selected working day and add it to the array if it is not already added.
     * @param event the event triggered by the user.
     */
    function workingDaysHandler(event) {
        switch (event.target.id) {
            case 'checkbox-mo':
                if ( event.target.checked ) {
                    workingDays.includes("Monday") ? console.log('Already added') : setWorkingDays([...workingDays, 'Monday']);
                } else {
                    workingDays.includes("Monday") ? workingDays.splice(workingDays.indexOf("Monday"), 1) : console.log('Already removed');
                }
                break;
            case 'checkbox-tu':
                if ( event.target.checked ) {
                    workingDays.includes("Tuesday") ? console.log('Already added') : setWorkingDays([...workingDays, 'Tuesday']);
                } else {
                    workingDays.includes("Tuesday") ? workingDays.splice(workingDays.indexOf("Tuesday"), 1) : console.log('Already removed');
                }
                break;
            case 'checkbox-we':
                if ( event.target.checked ) {
                    workingDays.includes("Wednesday") ? console.log('Already added') : setWorkingDays([...workingDays, 'Wednesday']);
                } else {
                    workingDays.includes("Wednesday") ? workingDays.splice(workingDays.indexOf("Wednesday"), 1) : console.log('Already removed');
                }
                break;
            case 'checkbox-th':
                if ( event.target.checked ) {
                    workingDays.includes("Thursday") ? console.log('Already added') : setWorkingDays([...workingDays, 'Thursday']);
                } else {
                    workingDays.includes("Thursday") ? workingDays.splice(workingDays.indexOf("Thursday"), 1) : console.log('Already removed');
                }
                break;
            case 'checkbox-fr':
                if ( event.target.checked ) {
                    workingDays.includes("Friday") ? console.log('Already added') : setWorkingDays([...workingDays, 'Friday']);
                } else {
                    workingDays.includes("Friday") ? workingDays.splice(workingDays.indexOf("Friday"), 1) : console.log('Already removed');
                }
                break;
            case 'checkbox-sa':
                if ( event.target.checked ) {
                    workingDays.includes("Saturday") ? console.log('Already added') : setWorkingDays([...workingDays, 'Saturday']);
                } else {
                    workingDays.includes("Saturday") ? workingDays.splice(workingDays.indexOf("Tuesday"), 1) : console.log('Already removed');
                }
                break;
            case 'checkbox-su':
                if ( event.target.checked ) {
                    workingDays.includes("Sunday") ? console.log('Already added') : setWorkingDays([...workingDays, 'Sunday']);
                } else {
                    workingDays.includes("Sunday") ? workingDays.splice(workingDays.indexOf("Sunday"), 1) : console.log('Already removed');
                }
                break;
            default:
                console.log('Nothing to do - not a valid weekday!');
        }
    }

    /**
     * Set the username that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function usernameChangeHandler(event) {
        setUsername(event.target.value);
    }

    /**
     * Set the password that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function passwordChangeHandler(event) {
        setPassword(event.target.value);
    }

    /**
     * Set the confirm password that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function confirmPasswordChangeHandler(event) {
        setConfirmPassword(event.target.value);
    }

    /**
     * Set the date of birth that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function dateOfBirthChangeHandler(event) {
        setDateOfBirth(event.target.value);
    }

    /**
     * Set the start date that the user entered to the state for later.
     * @param event the event triggered by the user.
     */
    function startDateChangeHandler(event) {
        setStartDate(event.target.value)
    }

    /**
     * Method to reset form to initial values.
     */
    function resetForm () {
        setFirstName("");
        setLastName("");
        setCompany(companies[0]);
        setPosition("");
        setUsername("");
        setPassword("");
        setConfirmPassword("");
        setStartDate("");
        setDateOfBirth("");
        setRole("Employee");
        document.getElementById("checkbox-mo").checked = false;
        document.getElementById("checkbox-tu").checked = false;
        document.getElementById("checkbox-we").checked = false;
        document.getElementById("checkbox-th").checked = false;
        document.getElementById("checkbox-fr").checked = false;
        document.getElementById("checkbox-sa").checked = false;
        document.getElementById("checkbox-su").checked = false;
        setWorkingDays([]);
    }

    /**
     * Register the new user by sending the request to the API. Display an alert and then move back to the home page.
     */
    function registerUser() {
        // Check that the passwords were identical, otherwise do nothing and show error message.
        if ( password !== confirmPassword ) {
            alert('The two passwords did not match. Please check the passwords and try again');
        }
        else {
            let startDateSplit = startDate.split("-");
            let dateOfBirthSplit = dateOfBirth.split("-");
            axios.post('http://localhost:8150/api/user/', {
                firstName: firstName,
                surname: lastName,
                username: username,
                password: password,
                company: company,
                workingDays: workingDays.join(","),
                position: position,
                startDate: startDateSplit[2] + '-' + startDateSplit[1] + '-' + startDateSplit[0],
                role: role,
                dateOfBirth: dateOfBirthSplit[2] + '-' + dateOfBirthSplit[1] + '-' + dateOfBirthSplit[0]
            }).then(function (response) {
                if ( response.status === 201 ) {
                    if ( props.company === '' ) {
                        alert('Thank you for registering for PersonalMan. Your account was created successfully. Please login with your new account on the next screen.')
                        navigate("/")
                    } else {
                        props.handleAddUserClose();
                        window.location.reload();
                    }
                }
            }).catch(function (error) {
                console.log(error);
            });
        }
    }

    /**
     * Cancel the request to add a new user and move back to the home page.
     */
    function cancelRegistration() {
        if ( props.company === '' ) {
            navigate("/");
        } else {
            props.handleAddUserClose();
        }
    }

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <>
        <Container className="d-flex flex-column">
        <Form>

            <Form.Group as={Row} className="mb-3" controlId="formPlaintextFirstName">
                <Form.Label column sm="2">First Name:</Form.Label>
                <Col sm="10">
                    <Form.Control type="text" value={firstName} placeholder="First Name" onChange={firstNameChangeHandler}/>
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="mb-3" controlId="formPlaintextLastName">
                <Form.Label column sm="2">Surname:</Form.Label>
                <Col sm="10">
                    <Form.Control type="text" value={lastName} placeholder="Surname" onChange={lastNameChangeHandler}/>
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="mb-3" controlId="formPlaintextCompany">
                <Form.Label column sm="2">Company:</Form.Label>
                <Col sm="10">
                    <Form.Select value={company} aria-label="Company Name" onChange={companyChangeHandler}>
                        {companies.map(company => ( <option key={company}>{company}</option>))}
                    </Form.Select>
                </Col>
            </Form.Group>

            { ( props.company ==='' ) ? <p className="small text-center fw-bold mt-2 pt-1 mb-4">Your company does not yet exist in PersonalMan? <a href="registerCompany"
                className="link-danger">Register Company</a>
                </p> : <p></p> }


            <Form.Group as={Row} className="mb-3" controlId="formPlaintextPosition">
                <Form.Label column sm="2">Position:</Form.Label>
                <Col sm="10">
                    <Form.Control value={position} type="text" placeholder="Position" onChange={positionChangeHandler}/>
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="mb-3" controlId="formCheckWorkingDays">
                <Form.Label column sm="2">Working Days:</Form.Label>
                <Col sm="10">
                    <Form.Check
                        inline
                        label="Mo"
                        name="group1"
                        type="checkbox"
                        id={`checkbox-mo`}
                        onChange={workingDaysHandler}
                    />
                    <Form.Check
                        inline
                        label="Tu"
                        name="group1"
                        type="checkbox"
                        id={`checkbox-tu`}
                        onChange={workingDaysHandler}
                    />
                    <Form.Check
                        inline
                        label="We"
                        type="checkbox"
                        id={`checkbox-we`}
                        onChange={workingDaysHandler}
                    />
                    <Form.Check
                        inline
                        label="Th"
                        type="checkbox"
                        id={`checkbox-th`}
                        onChange={workingDaysHandler}
                    />
                    <Form.Check
                        inline
                        label="Fr"
                        type="checkbox"
                        id={`checkbox-fr`}
                        onChange={workingDaysHandler}
                    />
                    <Form.Check
                        inline
                        label="Sa"
                        type="checkbox"
                        id={`checkbox-sa`}
                        onChange={workingDaysHandler}
                    />
                    <Form.Check
                        inline
                        label="Su"
                        type="checkbox"
                        id={`checkbox-su`}
                        onChange={workingDaysHandler}
                    />
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="mb-3" controlId="formPlaintextEmail">
                <Form.Label column sm="2">Username:</Form.Label>
                <Col sm="10">
                    <Form.Control type="text" value={username} placeholder="Username" autoComplete="username" onChange={usernameChangeHandler}/>
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
                <Form.Label column sm="2">Password:</Form.Label>
                <Col sm="10">
                    <Form.Control type="password" value={password} placeholder="Password" autoComplete="current-password"
                                  onChange={passwordChangeHandler}/>
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="mb-3" controlId="formPlaintextConfirmPassword">
                <Form.Label column sm="2">Confirm Password:</Form.Label>
                <Col sm="10">
                    <Form.Control type="password" value={confirmPassword} placeholder="Password" autoComplete="current-password"
                                  onChange={confirmPasswordChangeHandler}/>
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="mb-3" controlId="formPlaintextDateOfBirth">
                <Form.Label column sm="2">Date of Birth:</Form.Label>
                <Col sm="10">
                    <Form.Control type="date" value={dateOfBirth} placeholder="yyyy-MM-dd" onChange={dateOfBirthChangeHandler}/>
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="mb-3" controlId="formPlaintextStartDate">
                <Form.Label column sm="2">Start Date:</Form.Label>
                <Col sm="10">
                    <Form.Control type="date" value={startDate} onChange={startDateChangeHandler}/>
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="mb-3" controlId="formPlaintextRole">
                <Form.Label column sm="2">Role:</Form.Label>
                <Col sm="10">
                    <Form.Select aria-label="Role" value={role} onChange={roleChangeHandler}>
                        <option key="employee">Employee</option>
                        <option key="admin">Admin</option>
                    </Form.Select>
                </Col>
            </Form.Group>

        </Form>
        </Container>

        <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
            <Row>
                <Col className="text-center">
                    <Button className="mb-0 px-5 me-2" size='lg' onClick={registerUser}>Register</Button>
                    { ( props.company ==='' ) ? <Button className="mb-0 px-5 me-2" size='lg' onClick={resetForm}>Reset</Button> : <p></p> }
                    <Button className="mb-0 px-5 me-2" size='lg' onClick={cancelRegistration}>Cancel</Button>
                </Col>
            </Row>
        </Container>
        </>
    );

}

export default AddUserForm;
