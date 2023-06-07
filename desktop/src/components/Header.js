import {Container, Nav, Navbar} from "react-bootstrap";
import React, {useEffect} from "react";
import logo from '../assets/personalmanlogo-icon.png';
import { Link } from 'react-router-dom';
import {useState} from "react";
import axios from "axios";

function Header(props) {

    const [name, setName] = useState("");
    const [role, setRole] = useState("");

    useEffect(() => {
        axios.get('http://localhost:8150/api/user/?company=' + props.company + '&username=' + props.token.split("-")[0] + '&token=' + props.token)
            .then(res => {
                const result = res.data;
                setName(result['firstName'] + ' ' + result['surname']);
                setRole(result['role']);
            })
    }, [props.company, props.token]);

    return (
        <Navbar bg="light" expand="md">
            <Container fluid>
                <Navbar.Brand><img src={logo} alt="PersonalMan Logo"
                                   className="img-responsive img-max-height"/></Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/absences" state={{token: props.token, company: props.company}} id="absences-link">Absences</Nav.Link>
                        <Nav.Link as={Link} to="/changePassword" state={{token: props.token, company: props.company}} id="password-link">Change Password</Nav.Link>
                        {role === 'Admin' &&
                            <Nav.Link as={Link} to="/users" state={{token: props.token, company: props.company}} id="users-link">Users</Nav.Link>
                        }
                    </Nav>
                </Navbar.Collapse>
                <Navbar.Collapse className="justify-content-end">
                    <Navbar.Text>
                        Signed in as: {name} ({role})
                    </Navbar.Text>
                    <Nav.Link as={Link} to="/logout" id="logout-link" className="p-1">Logout</Nav.Link>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default Header;
