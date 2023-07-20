import {Container, Nav, Navbar} from "react-bootstrap";
import React, {useEffect} from "react";
import logo from '../../../assets/personalmanlogo-icon.png';
import { Link } from 'react-router-dom';
import {useState} from "react";
import axios from "axios";
import PropTypes from 'prop-types';
import {useTranslation} from "react-i18next";

/**
 * This is the component which shows the header and navigation bar including logout option.
 * @param props company - the company that the user belongs to, token - the user access token for the currently logged in user.
 * @returns {JSX.Element} to be displayed to the user.
 */
function Header(props) {

    const [name, setName] = useState("");
    const [role, setRole] = useState("");

    const {t} = useTranslation();

    /**
     * Load the first name, surname and role of the currently logged in user to be displayed as part of the header bar.
     */
    useEffect(() => {
        axios.get(process.env.REACT_APP_SERVER_URL + '/user/?company=' + props.company + '&username=' + props.token.split("-")[0] + '&token=' + props.token)
            .then(res => {
                const result = res.data;
                setName(result['firstName'] + ' ' + result['surname']);
                setRole(result['role']);
            }).catch(error => {
                console.error(error);
        })
    }, [props.company, props.token]);

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Navbar bg="light" expand="md">
            <Container fluid>
                <Navbar.Brand><img src={logo} alt="PersonalMan Logo"
                                   className="img-responsive img-max-height"/></Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/absences" state={{token: props.token, company: props.company}} id="absences-link">{t('headerAbsencesLink')}</Nav.Link>
                        <Nav.Link as={Link} to="/changePassword" state={{token: props.token, company: props.company}} id="password-link">{t('headerPasswordLink')}</Nav.Link>
                        {role === 'Admin' &&
                            <Nav.Link as={Link} to="/users" state={{token: props.token, company: props.company}} id="users-link">{t('headerUsersLink')}</Nav.Link>
                        }
                    </Nav>
                </Navbar.Collapse>
                <Navbar.Collapse className="justify-content-end">
                    <Navbar.Text>
                        {t('headerUserInfo')}: {name} ({role})
                    </Navbar.Text>
                    <Nav.Link as={Link} to="/logout" state={{token: props.token}} id="logout-link" className="p-1">{t('headerLogoutLink')}</Nav.Link>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default Header;

Header.propTypes = {
    /**
     * the company that the user belongs to
     */
    company: PropTypes.string,

    /**
     * the user access token for the currently logged in user
     */
    token: PropTypes.string
}

Header.defaultProps = {
    company: 'Required',
    token: 'Required'
};
