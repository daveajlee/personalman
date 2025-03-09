import React from "react";
import {Accordion, Card, Col, Container, Row} from "react-bootstrap";
import roadmapImage from "../jess-bailey-mexeVPlTB6k-unsplash.jpg";

const Roadmap = () => (
    <Container fluid>
        <Row>
            <Col>
                <Card className="border-0">
                    <Card.Img variant="top" className="img-responsive w-25 rounded mx-auto d-block mt-3" src={roadmapImage} />
                    <Card.Title as="h3" className="mt-5 text-center">Roadmap</Card.Title>
                    <Card.Body className="text-center mb-5">This page contains a list of features that I plan to add
                        to PersonalMan in the future. It is sorted like a product backlog with the features at the top
                        having more chance of being implemented before the features near the bottom. Since I work on
                        PersonalMan in my free time, I cannot guarantee any deadlines when features will be available.
                        You can also get in touch with me if you would like to help with the development of PersonalMan
                        so that features can be implemented more quickly!</Card.Body>
                </Card>
            </Col>
        </Row>
        <Row>
            <Col>
                <Accordion defaultActiveKey="0">
                    <Accordion.Item eventKey="1">
                        <Accordion.Header>Backup Organisations</Accordion.Header>
                        <Accordion.Body>
                            In order to make PersonalMan more reliable, it should be possible to export and import
                            all organisation data. This should include all of the employee data and be encrypted.
                            This would also make it possible for organisations to backup the data to prevent data loss.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="2">
                        <Accordion.Header>Teams and Departments</Accordion.Header>
                        <Accordion.Body>
                            Currently users / employees can only have a particular job title within the organisation
                            but are not assigned to any department. Adding teams and departments would make it possible
                            to better reflect the structure of the organisation - including who is responsible for the
                            employee as line manager.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="3">
                        <Accordion.Header>Meeting Rooms</Accordion.Header>
                        <Accordion.Body>
                            Even though a lot of organisations are doing hybrid working, there are still a number of
                            organisations which work at least two to three days a week in an office environment. These
                            organisations would benefit from the additional of a booking system for meeting rooms. Whilst
                            this can be done within Outlook or other tools, PersonalMan could use the absence management
                            information to cancel meetings in meeting rooms where the organiser is ill or absence for
                            any other reason.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="4">
                        <Accordion.Header>User Permissions</Accordion.Header>
                        <Accordion.Body>
                            Currently PersonalMan only supports standard users and admin users. Further user permissions
                            and roles should be added to create roles such as line manager to approve absences or to see
                            the information of some employees but not all employees in the organisation.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="5">
                        <Accordion.Header>Approval Workflow for Absences</Accordion.Header>
                        <Accordion.Body>
                            Currently all of the absences are automatically approved and entered into the calendar
                            without any manual approval steps. Whilst this may be true of illness, it would be better
                            that holidays and conferences are approved from the line manager.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="6">
                        <Accordion.Header>User Profile</Accordion.Header>
                        <Accordion.Body>
                            It would be beneficial for everyone in the organisation to see basic information about
                            other employees such as name, job title and the training courses that they attended. This
                            would allow users to search for particular skilled employees who could help them with their
                            current tasks.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="7">
                        <Accordion.Header>Overtime Management</Accordion.Header>
                        <Accordion.Body>
                            It should be possible to calculate how much overtime a particular user has worked over a
                            particular time period and record how this overtime will be reduced. It should also be
                            prevented that an employee works more than the legal number of hours per day.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="8">
                        <Accordion.Header>Fixed Salary</Accordion.Header>
                        <Accordion.Body>
                            It should be possible to pay the employee a fixed salary which is not calculated on
                            the number of hours that a user has worked.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="9">
                        <Accordion.Header>Mobile App</Accordion.Header>
                        <Accordion.Body>
                            In order to make PersonalMan even easier to use, it would be better to have a mobile
                            app which users could download onto their smartphone. This app should offer similar
                            functionality to the browser-based administration portal.
                        </Accordion.Body>
                    </Accordion.Item>
                </Accordion>
            </Col>
        </Row>
    </Container>
);

export default Roadmap;
