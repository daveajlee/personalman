import {Container, Nav, Navbar} from "react-bootstrap";
import React from "react";
import logo from '../assets/personalmanlogo-icon.png';
//import { Link } from 'react-router-dom';

const Header = () => (

    <Navbar bg="light" expand="lg">
        <Container>
            <Navbar.Brand><img src={logo} alt="PersonalMan"
                                                className="img-responsive img-max-height"/></Navbar.Brand>
            {/* <Navbar.Brand as={Link} to="/"><img src={logo} alt="PersonalMan"
                                                className="img-responsive img-max-height"/></Navbar.Brand> */}
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                    <Nav.Link>Home</Nav.Link>
                    {/* <Nav.Link as={Link} to="/">Home</Nav.Link> */}
                </Nav>
            </Navbar.Collapse>
        </Container>
    </Navbar>
);

export default Header;
