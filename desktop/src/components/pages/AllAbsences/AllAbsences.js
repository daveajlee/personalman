import React, {useEffect} from "react";
import {useLocation} from "react-router-dom";
import {Container} from "react-bootstrap";
import Header from "../../layout/Header/Header";
import {useState} from "react";
import axios from "axios";
import AbsenceList from "../../lists/AbsenceList/AbsenceList";

/**
 * This is the page which displays the list of absences for all users for an admin user within a particular company.
 * @returns {JSX.Element} to be displayed to the user.
 */
function AllAbsences(props) {

    const location = useLocation();

    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [role, setRole] = useState("");

    /**
     * Retrieve the current token either from the supplied state or empty if we are in doc mode.
     */
    const token = (props.docMode && props.docMode==='true') ? "" : location.state.token;

    /**
     * Retrieve the company either from the supplied state or empty if we are in doc mode.
     */
    const company = ( props.docMode && props.docMode==='true') ? "" : location.state.company;

    /**
     * Retrieve the year either from the supplied state or empty if we are in doc mode.
     */
    const year = ( props.docMode && props.docMode==='true') ? "" :  location.state.year ? location.state.year : new Date().getFullYear();

    /**
     * Retrieve the month either from the supplied state or empty if we are in doc mode.
     */
    const month = ( props.docMode && props.docMode==='true') ? "" : location.state.month ? location.state.month : ((new Date().getMonth()) +1);

    /**
     * Load the absences from the REST API either for all users within a specific time period
     * (usually a month in a year).
     */
    useEffect(() => {
        // Ensure that the user is actually admin, otherwise they cannot view the page.
        axios.get(process.env.REACT_APP_SERVER_URL + '/user/?company=' + company + '&username=' + token.split("-")[0] + '&token=' + token)
                .then(res => {
                    const result = res.data;
                    setRole(result['role']);
                }).catch(error => {
                    console.error(error);
        })
        // Load the actual month of absences.
        /*if ( month === null ) {
            setStartDate('01-' + ((new Date().month) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear());
            setEndDate(daysInMonth(((new Date().month) +1), new Date().getFullYear()) + '-' + ((new Date().month) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear());
        } else {*/
            setStartDate('01-' + (month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + year);
            setEndDate(daysInMonth(month, year) + '-' + (month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + year);
        //}

    }, [token, month, year, company]);

    /**
     * Return the number of days that a month has in a particular year.
     * @param month the month to retrieve the number of days for.
     * @param year the year that should be taken into account.
     * @returns {number} the number of days that the month has in the supplied year.
     */
    function daysInMonth (month, year) {
        return new Date(year, month, 0).getDate();
    }

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Container fluid>
            <Header token={token} company={company}/>

            { role==='Admin' && <AbsenceList company={company} token={token} username='' startDate={startDate} endDate={endDate}
                         month={month}
                         year={year}/>}

        </Container>
    )


}

export default AllAbsences;
