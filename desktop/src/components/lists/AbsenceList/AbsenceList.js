import {Button, Col, Container, Row} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import PropTypes from "prop-types";
import {useTranslation} from "react-i18next";

/**
 * This component displays the list of absences that have been saved in the system for either a particular username
 * if supplied or all users if no username has been supplied.
 * @param props company - name of company to display absences for (required), username - username to display absences for
 * (optional), startDate - date in format dd-MM-yyyy to start displaying absences from, endDate - date in format dd-MM-yyyy
 * which is the last date to display absences for, token - the current access token from the user wishing to view the absences,
 * month - the month to display absences, year - the year to display absences.
 * @returns {JSX.Element} to be displayed to the user.
 */
function AbsenceList (props) {

    const [absences, setAbsences] = useState([]);

    const navigate = useNavigate();

    const {t} = useTranslation();

    const months    = [t('january'),t('february'),t('march'),t('april'),t('may'),t('june'),
        t('july'),t('august'),t('september'),t('october'),t('november'),t('december')];

    /**
     * Load the absences from the REST API either for a particular username or all users within a specific time period
     * (usually a month in a year).
     */
    useEffect(() => {
        if ( props.startDate && props.endDate ) {
            if ( props.username ) {
                axios.get(process.env.REACT_APP_SERVER_URL + '/absences/?company=' + props.company + '&username=' + props.username + '&startDate=' + props.startDate + '&endDate=' + props.endDate + '&onlyCount=false&token=' + props.token)
                    .then(res => {
                        const result = res.data;
                        setAbsences(result['absenceResponseList']);
                    }).catch(error => {
                        console.error(error);
                })
            } else {
                axios.get(process.env.REACT_APP_SERVER_URL + '/absences/?company=' + props.company + '&startDate=' + props.startDate + '&endDate=' + props.endDate + '&onlyCount=false&token=' + props.token)
                    .then(res => {
                        const result = res.data;
                        setAbsences(result['absenceResponseList']);
                    }).catch(error => {
                        console.error(error);
                })
            }
        } else {
            let startDate = '01-'+ props.month.toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + props.year;
            let endDate = '31-' + props.month.toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + props.year
            if ( props.username ) {
                axios.get(process.env.REACT_APP_SERVER_URL + '/absences/?company=' + props.company + '&username=' + props.username + '&startDate=' + startDate + '&endDate=' + endDate + '&onlyCount=false&token=' + props.token)
                    .then(res => {
                        const result = res.data;
                        setAbsences(result['absenceResponseList']);
                    })
                    .catch(error => {
                        console.error(error);
                    })
            } else {
                axios.get(process.env.REACT_APP_SERVER_URL + '/absences/?company=' + props.company + '&startDate=' + startDate + '&endDate=' + endDate + '&onlyCount=false&token=' + props.token)
                    .then(res => {
                        const result = res.data;
                        setAbsences(result['absenceResponseList']);
                    }).catch(error => {
                        console.error(error);
                } )
            }

        }
    }, [props.token, props.company, props.startDate, props.endDate, props.month, props.year, props.username]);

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
                if ( props.username ) {
                    navigate("/absences", {state:{token: props.token, month: month, year: year, company: props.company }})
                } else {
                    navigate("/allAbsences", {state:{token: props.token, month: month, year: year, company: props.company }})
                }
            } else {
                if ( props.username ) {
                    navigate("/absences", {state:{token: props.token, month: month, year: new Date().getFullYear(), company: props.company }})
                } else {
                    navigate("/allAbsences", {state:{token: props.token, month: month, year: new Date().getFullYear(), company: props.company }})
                }

            }
        } else {
            let month = new Date().getMonth() + 1;
            if ( month < 1 ) {
                month = 12;
            } else {
                month -= 1;
            }
            if ( props.username ) {
                navigate("/absences", {
                    state: {
                        token: props.token,
                        month: month,
                        year: new Date().getFullYear(),
                        company: props.company
                    }
                })
            } else {
                navigate("/allAbsences", {
                    state: {
                        token: props.token,
                        month: month,
                        year: new Date().getFullYear(),
                        company: props.company
                    }
                })
            }
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
                if ( props.username ) {
                    navigate("/absences", {state:{token: props.token, month: month, year: year, company: props.company }})
                } else {
                    navigate("/allAbsences", {state:{token: props.token, month: month, year: year, company: props.company }})
                }

            } else {
                if ( props.username ) {
                    navigate("/absences", {state:{token: props.token, month: month, year: new Date().getFullYear(), company: props.company }})
                } else {
                    navigate("/allAbsences", {state:{token: props.token, month: month, year: new Date().getFullYear(), company: props.company }})
                }
            }
        } else {
            let month = new Date().getMonth() + 1;
            if ( month >= 12 ) {
                month = 1;
            } else {
                month += 1;
            }
            if ( props.username ) {
                navigate("/absences", {
                    state: {
                        token: props.token,
                        month: month,
                        year: new Date().getFullYear(),
                        company: props.company
                    }
                })
            } else {
                navigate("/allAbsences", {
                    state: {
                        token: props.token,
                        month: month,
                        year: new Date().getFullYear(),
                        company: props.company
                    }
                })
            }
        }
    }

    /**
     * Delete the supplied absence.
     * @param absence the absence to delete
     */
    function deleteAbsence(absence) {
        axios.delete(process.env.REACT_APP_SERVER_URL + '/absences/?company=' + props.company + '&username=' + absence.username
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

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Container>
            <Container fluid className="p-3 my-5 h-custom">
                <Row className="d-flex flex-row align-items-center justify-content-center">
                    <Col>
                        {props.username ? <h1 className="text-center">{t('absenceListAbsenceFor')}: {props.username}</h1> : <h1 className="text-center">{t('absenceListAllAbsences')}</h1> }
                    </Col>
                </Row>
                <Row className="d-flex flex-row align-items-center justify-content-center mb-5">
                    <Col>
                        <h1 className="text-center">{getMonthName()} {getYear()}</h1>
                    </Col>
                </Row>
                {absences.map(d => (<Row className="align-items-center justify-content-center" key={d.startDate + '-' + d.endDate}>
                    <Col xs lg="10">

                        <h4 className="text-center">{t('absenceListAbsence', { startDate: d.startDate, endDate: d.endDate, category: d.category, username: d.username })}</h4>
                    </Col>
                    <Col >
                        <Button variant="danger" size='sm' onClick={deleteAbsence.bind(this, d)}>{t('absenceListDeleteButton')}</Button>
                    </Col>
                </Row>))}
            </Container>

            <Container className='align-items-center justify-content-center text-md-start mt-4 pt-2'>
                <Row>
                    <Col className="text-center">
                        <Button className="mb-0 px-5 me-2 mt-2" size='lg' onClick={previousMonth}>{t('absenceListPreviousMonth')}</Button>
                        <Button className="mb-0 px-5 me-2 mt-2" size='lg' onClick={nextMonth}>{t('absenceListNextMonth')}</Button>
                    </Col>
                </Row>
            </Container>
        </Container>
    )

}

export default AbsenceList;

AbsenceList.propTypes = {
    /**
     * name of company to display absences for
     */
    company: PropTypes.string,

    /**
     * username to display absences for
     */
    username: PropTypes.string,

    /**
     * date in format dd-MM-yyyy to start displaying absences from
     */
    startDate: PropTypes.string,

    /**
     * date in format dd-MM-yyyy which is the last date to display absences for
     */
    endDate: PropTypes.string,

    /**
     * the current access token from the user wishing to view the absences
     */
    token: PropTypes.string,

    /**
     * the month to display absences
     */
    month: PropTypes.number,

    /**
     * the year to display absences
     */
    year: PropTypes.number,
}

AbsenceList.defaultProps = {
    company: 'Required',
    username: 'Optional',
    token: 'Required',
    startDate: 'Required',
    endDate: 'Required',
};

