import React, {useEffect} from "react";
import {useLocation} from "react-router-dom";
import {Container} from "react-bootstrap";
import Header from "../components/Header";
import {useState} from "react";
import axios from "axios";
import AbsenceList from "../components/AbsenceList";

function AllAbsences() {

    const location = useLocation();

    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [role, setRole] = useState("");


    useEffect(() => {
        // Ensure that the user is actually admin, otherwise they cannot view the page.
        axios.get('http://localhost:8150/api/user/?company=' + location.state.company + '&username=' + location.state.token.split("-")[0] + '&token=' + location.state.token)
                .then(res => {
                    const result = res.data;
                    setRole(result['role']);
                })
        // Load the actual month of absences.
        if ( !location.state.month ) {
            setStartDate('01-' + ((new Date().getMonth()) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear());
            setEndDate(daysInMonth(((new Date().getMonth()) +1), new Date().getFullYear()) + '-' + ((new Date().getMonth()) +1).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + new Date().getFullYear());
        } else {
            setStartDate('01-' + (location.state.month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + location.state.year);
            setEndDate(daysInMonth(location.state.month, location.state.year) + '-' + (location.state.month).toLocaleString('en-US', {
                minimumIntegerDigits: 2,
                useGrouping: false
            }) + '-' + location.state.year);
        }

    }, [location.state.token, location.state.month, location.state.year, location.state.company]);

    function daysInMonth (month, year) {
        return new Date(year, month, 0).getDate();
    }

    return (
        <Container fluid>
            <Header token={location.state.token} company={location.state.company}/>

            { role==='Admin' && <AbsenceList company={location.state.company} token={location.state.token} startDate={startDate} endDate={endDate}
                         month={location.state.month ? location.state.month : ((new Date().getMonth()) +1)}
                         year={location.state.year ? location.state.year : new Date().getFullYear()}/>}

        </Container>
    )


}

export default AllAbsences;
