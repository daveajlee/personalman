import {Button, Col, Container, Row} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function AbsenceList (props) {

    const [absences, setAbsences] = useState([]);
    const months    = ['January','February','March','April','May','June','July','August','September','October','November','December'];
    const navigate = useNavigate();

    useEffect(() => {
        if ( props.startDate && props.endDate ) {
            axios.get('http://localhost:8150/api/absences/?company=' + props.company + '&username=' + props.token.split("-")[0] + '&startDate=' + props.startDate + '&endDate=' + props.endDate + '&onlyCount=false&token=' + props.token)
                .then(res => {
                    const result = res.data;
                    setAbsences(result['absenceResponseList']);
                })
        } else {
            let startDate = '01-'+ props.month.toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + props.year;
            let endDate = '31-' + props.month.toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + props.year
            axios.get('http://localhost:8150/api/absences/?company=' + props.company + '&username=' + props.token.split("-")[0] + '&startDate=' + startDate + '&endDate=' + endDate + '&onlyCount=false&token=' + props.token)
                .then(res => {
                    const result = res.data;
                    setAbsences(result['absenceResponseList']);
                })
        }
    }, [props.token, props.company, props.startDate, props.endDate, props.month, props.year]);

    /**
     * Get the username of the currently logged in user who we will display absences for.
     * @returns the username
     */
    function getUsername() {
        return props.token.split("-")[0];
    }

    /**
     * Get the name of the current month that we are in.
     * @returns the name of the month in English
     */
    function getMonthName() {
        if ( props.month >= 1 ) {
            return months[props.month-1];
        } else {
            return months[new Date().getMonth()];
        }
    }

    /**
     * Get the current year which can be influenced by the previous and next month buttons.
     * @returns the current year to be displayed.
     */
    function getYear() {
        if ( props.year ) {
            return props.year;
        } else {
            return new Date().getFullYear();
        }
    }

    /**
     * Move back to the previous month.
     */
    function previousMonth() {
        if ( props.month ) {
            let month = props.month; let year = props.year;
            if ( props.month <= 1 ) {
                month = 12;
                if ( props.year ) {
                    year -= 1;
                }
            } else {
                month -= 1;
            }
            if ( props.year ) {
                navigate("/absences", {state:{token: props.token, month: month, year: year, company: props.company }})
            } else {
                navigate("/absences", {state:{token: props.token, month: month, year: new Date().getFullYear(), company: props.company }})
            }
        } else {
            let month = new Date().getMonth() + 1;
            if ( month < 1 ) {
                month = 12;
            } else {
                month -= 1;
            }
            navigate("/absences", {state:{token: props.token, month: month, year: new Date().getFullYear(), company: props.company }})
        }
    }

    /**
     * Move forward to the next month.
     */
    function nextMonth() {
        if ( props.month ) {
            let month = props.month; let year = props.year;
            if ( props.month >= 12 ) {
                month = 1;
                if ( props.year ) {
                    year += 1;
                }
            } else {
                month += 1;
            }
            if ( props.year ) {
                navigate("/absences", {state:{token: props.token, month: month, year: year, company: props.company }})
            } else {
                navigate("/absences", {state:{token: props.token, month: month, year: new Date().getFullYear(), company: props.company }})
            }
        } else {
            let month = new Date().getMonth() + 1;
            if ( month >= 12 ) {
                month = 1;
            } else {
                month += 1;
            }
            navigate("/absences", {state:{token: props.token, month: month, year: new Date().getFullYear(), company: props.company }})
        }
    }

    /**
     * Delete the supplied absence.
     * @param absence the absence to delete
     */
    function deleteAbsence(absence) {
        axios.delete('http://localhost:8150/api/absences/?company=' + props.company + '&username=' + absence.username
            + '&startDate=' + absence.startDate + '&endDate=' + absence.endDate + '&token=' + props.token)
            .then(function (response) {
                if ( response.status === 200 ) {
                    alert('Absence was deleted successfully!');
                    window.location.reload();
                }
            }).catch(function (error) {
            console.log(error);
        });
    }

    return (
        <Container>
            <Container fluid className="p-3 my-5 h-custom">
                <Row className="d-flex flex-row align-items-center justify-content-center">
                    <Col>
                        <h1 className="text-center">Absences for user: {getUsername()}</h1>
                    </Col>
                </Row>
                <Row className="d-flex flex-row align-items-center justify-content-center mb-5">
                    <Col>
                        <h1 className="text-center">{getMonthName()} {getYear()}</h1>
                    </Col>
                </Row>
                {absences.map(d => (<Row className="align-items-center justify-content-center" key={d.startDate + '-' + d.endDate}>
                    <Col xs lg="10">
                        <h4 className="text-center">{d.startDate} to {d.endDate} with {d.category} </h4>
                    </Col>
                    <Col >
                        <Button variant="danger" size='sm' onClick={deleteAbsence.bind(this, d)}>Delete</Button>
                    </Col>
                </Row>))}
            </Container>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2 mt-2" size='lg' onClick={previousMonth}>Previous Month</Button>
                        <Button className="mb-0 px-5 me-2 mt-2" size='lg' onClick={nextMonth}>Next Month</Button>
                    </Col>
                </Row>
            </Container>
        </Container>
    )

}

export default AbsenceList;
