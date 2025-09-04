import {Container, Nav, Navbar} from "react-bootstrap";
import {useEffect} from "react";
import logo from '../../../assets/personalmanlogo-icon.png';
import { Link } from 'react-router-dom';
import {useState} from "react";
import axios from "axios";
import PropTypes from 'prop-types';
import {useTranslation} from "react-i18next";
import * as React from "react";

type HeaderProps = {
    company: string;
    token: string;
}

/**
 * This is the component which shows the header and navigation bar including logout option.
 * @param props company - the company that the user belongs to, token - the user access token for the currently logged in user.
 * @returns {JSX.Element} to be displayed to the user.
 */
function Header({company, token}: HeaderProps): React.JSX.Element {

    const [name, setName] = useState("");
    const [role, setRole] = useState("");

    const {t} = useTranslation();

    /**
     * Load the first name, surname and role of the currently logged in user to be displayed as part of the header bar.
     */
    useEffect(() => {
        axios.get(import.meta.env.VITE_SERVER_URL + '/user/?company=' + company + '&username=' + token.split("-")[0] + '&token=' +  token)
            .then(res => {
                const result = res.data;
                setName(result['firstName'] + ' ' + result['surname']);
                setRole(result['role']);
            }).catch(error => {
                console.error(error);
        })
    }, [ company, token]);

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
                        <Nav.Link as={Link} to="/absences" state={{token:  token, company:  company}} id="absences-link">{t('headerAbsencesLink')}</Nav.Link>
                        <Nav.Link as={Link} to="/changePassword" state={{token: token, company:  company}} id="password-link">{t('headerPasswordLink')}</Nav.Link>
                        {role === 'Admin' &&
                            <Nav.Link as={Link} to="/users" state={{token: token, company: company}} id="users-link">{t('headerUsersLink')}</Nav.Link>
                        }
                    </Nav>
                </Navbar.Collapse>
                <Navbar.Collapse className="justify-content-end">
                    <Navbar.Text>
                        {t('headerUserInfo')}: {name} ({role})
                    </Navbar.Text>
                    <Nav.Link as={Link} to="/logout" state={{token: token}} id="logout-link" className="p-1">{t('headerLogoutLink')}</Nav.Link>
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
