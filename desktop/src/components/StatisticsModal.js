import {Button, Modal} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import axios from "axios";

function StatisticsModal (props) {

    const [statisticsMap, setStatisticsMap] = useState([]);
    const [leaveEntitlement, setLeaveEntitlement] = useState(0);

    const handleStatisticsClose = () => props.setShowStatisticsModal(false);

    useEffect(() => {
        // Load the statistics for the current year.
        let startYearDate = '01-01-' + props.year;
        let endYearDate = '31-12-' + props.year;
        axios.get('http://localhost:8150/api/absences/?company=' + props.company + '&username=' + props.token.split("-")[0] + '&startDate=' + startYearDate + '&endDate=' + endYearDate + '&onlyCount=false&token=' + props.token)
            .then(res => {
                const result = res.data;
                setStatisticsMap(result['statisticsMap']);
            })
        // Get the leave entitlement for this user.
        axios.get('http://localhost:8150/api/user/?company=' + props.company + '&username=' + props.token.split("-")[0] + '&token=' + props.token)
            .then(res => {
                const result = res.data;
                setLeaveEntitlement(result['leaveEntitlementPerYear']);
            })
    }, [props.company, props.token, props.year]);

    return (
        <Modal show={props.showStatisticsModal} onHide={props.handleStatisticsClose}>
            <Modal.Header closeButton>
                <Modal.Title>View Statistics - {props.username} - {props.year}</Modal.Title>
            </Modal.Header>
            <Modal.Body>Illness: {statisticsMap['Illness']} days <br/> Holiday: {statisticsMap['Holiday']} days (Remaining: {leaveEntitlement} days) <br/>
                Trip: {statisticsMap['Trip']} days <br/> Conference: {statisticsMap['Conference']} days <br/> Day in Lieu: {statisticsMap['Day in Lieu']} days (Remaining: {statisticsMap['Day in Lieu Request']} days) <br/>
                Federal Holiday: {statisticsMap['Federal Holiday']} days</Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={handleStatisticsClose}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
    );

}

export default StatisticsModal;
