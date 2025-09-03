import {Col, Container, Image, Row} from "react-bootstrap";
import AddUserForm from "../../forms/AddUserForm/AddUserForm";
import {useTranslation} from "react-i18next";
import logo from './../../../assets/personalman-logo.png';
import * as React from "react";

type RegisterUserProps = {
    companyName: string;
    token: string;
}

/**
 * This is the page which allows the user to register themselves for PersonalMan.
 * @returns {JSX.Element} to be displayed to the user.
 */
function RegisterUser ({companyName, token}: RegisterUserProps): React.JSX.Element {

    const {t} = useTranslation();

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
                            <h3>{t('registerUserTitle')}</h3>
                        </Col>
                    </Row>
                </Container>

                <AddUserForm companyName={companyName} token={token} handleAddUserClose={() => {}}/>

            </Col>
        </Row>
    </Container>)

}

export default RegisterUser;
