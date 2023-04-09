import {Container, Nav, Navbar} from "react-bootstrap";
import React from "react";
import logo from '../assets/personalmanlogo-icon.png';
import { Link } from 'react-router-dom';

const Header = () => (

    <Navbar bg="light" expand="md">
        <Container>
            <Navbar.Brand><img src={logo} alt="PersonalMan Logo"
                                                className="img-responsive img-max-height"/></Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                    <Nav.Link as={Link} to="/profile" id="profile-link">Profile</Nav.Link>
                    <Nav.Link as={Link} to="/absences" id="absences-link">Absences</Nav.Link>
                    <Nav.Link as={Link} to="/logout" id="logout-link">Logout</Nav.Link>
                </Nav>
            </Navbar.Collapse>
        </Container>
    </Navbar>
);

export default Header;
