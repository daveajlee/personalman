import React from "react";
import {Accordion, Card, Col, Container, Row} from "react-bootstrap";
import roadmapImage from "../jess-bailey-mexeVPlTB6k-unsplash.jpg";

const Features = () => (
    <Container fluid>
        <Row>
            <Col>
                <Card>
                    <Card.Img variant="top" className="img-responsive w-25 rounded mx-auto d-block mt-3" src={roadmapImage} />
                    <Card.Title className="mt-5 text-center">Features</Card.Title>
                    <Card.Body className="text-center mb-5">This page contains information about features that are already
                    implemented in PersonalMan. <br/> Please visit the Roadmap page if you would like to find out more
                    about new features that are planned.</Card.Body>
                </Card>
            </Col>
        </Row>
        <Row>
            <Col>
                <Accordion defaultActiveKey="0">
                    <Accordion.Item eventKey="0">
                        <Accordion.Header>Organisations</Accordion.Header>
                        <Accordion.Body>
                            Each organisation contains a set of users (employees) who can be managed. Before creating
                            an employee for an organisation, the organisation itself must be created. This organisation
                            has a name, a country where it is located (e.g. to calculate public holidays) and
                            the standard number of holidays that an employee working for the company has per year.
                            The number of holidays per year can be varied for a particular employee if desired.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="1">
                        <Accordion.Header>Employees / Users</Accordion.Header>
                        <Accordion.Body>
                            Each employee of an organisation has a separate user account in PersonalMan which
                            is secured by a username and password. An employee can either be a standard user or an
                            admin user. In the case of admin users, the employee can add other employees to PersonalMan.
                            An employee has a name, a list of days that the employee usually works (which is necessary
                            to calculate public holiday entitlement), their job title, their date of birth, the
                            date that they started working for the company and the number of holidays per year
                            (if the standard number of holidays should not be used). If an employee chooses to leave
                            the organisation, an admin user can remove their account.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="2">
                        <Accordion.Header>Absence Management</Accordion.Header>
                        <Accordion.Body>
                            Each user who has a user account and is logged into the PersonalMan system, may add
                            an absence for a particular date or dates. The currently supported list of reasons for
                            an absence are: illness, holiday (annual leave), an official trip for the organisation,
                            attending a conference for the organisation and public holidays. A user cannot apply
                            for more holidays than their leave entitlement is for the current year. A user may
                            also be absent on days where they would normally not work e.g. an official trip over a
                            weekend - in this case the user automatically gets days in lieu which they may take
                            as time off (another form of absence) at a later day. A user cannot take more time off
                            than they have already accumulated through days in lieu. Currently all of the absences
                            are automatically approved and entered into the calendar without any manual approval steps.
                            Standard users can only see their own absences. Admin users can see all absences within
                            their organisation.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="3">
                        <Accordion.Header>Training Courses</Accordion.Header>
                        <Accordion.Body>
                            Each employee has a list of training courses that they have attended. This can be added
                            to at any point with new training courses that the employee has attended.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="4">
                        <Accordion.Header>Timesheets</Accordion.Header>
                        <Accordion.Body>
                            Each employee can enter the number of hours that they have worked on a particular date.
                            This is particularly relevant for those employees who are paid based on the number of
                            hours that they work. It is possible to increase the number of hours later if the employee
                            has on-call duty and has to work part of the evening as well.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="5">
                        <Accordion.Header>Salary</Accordion.Header>
                        <Accordion.Body>
                            Each user or employee may be paid a salary. This salary is an hourly wage which is paid
                            based on the number of hours worked in a particular week. It is possible to pay all employees
                            who have worked hours within a certain date range (e.g. the month of March in a particular year).
                            The particular amount that the user should be paid is then calculated. The actual
                            payment processing must be done using separate software. It is then possible to mark
                            that all of the employees have been paid. The salary of each employee can be updated at any time.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="6">
                        <Accordion.Header>User Administration</Accordion.Header>
                        <Accordion.Body>
                            A user can change their password at any time as long as they can enter their old password.
                            If the user has forgotten their password, then an admin user can reset their password.
                            An admin user can also deactivate users within the organisation for whatever reason.
                        </Accordion.Body>
                    </Accordion.Item>
                    <Accordion.Item eventKey="7">
                        <Accordion.Header>User History</Accordion.Header>
                        <Accordion.Body>
                            The most critical dates in an employee's journey with the organisation such as starting day,
                            being paid and the date that they leave are stored in the user's profile including the reason
                            why they left the organisation.
                        </Accordion.Body>
                    </Accordion.Item>
                </Accordion>
            </Col>
        </Row>
    </Container>
);

export default Features;
