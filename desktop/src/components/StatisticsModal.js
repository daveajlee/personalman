import {Button, Modal} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import axios from "axios";

/**
 * This is the modal window to show the view statistics for a particular user. The modal is used for admin users who want
 * to view the statistics of a particular user.
 * @param props setShowStatisticsModal - function to determine whether modal should be shown or not,
 * company - the company that the user belongs to, username - the username of the user that the statistics should be displayed for,
 * token - the current user access token of the admin user, year - the year that the statistics should be displayed for
 * @returns {JSX.Element} to be displayed to the user.
 */
function StatisticsModal (props) {

    const [statisticsMap, setStatisticsMap] = useState([]);
    const [leaveEntitlement, setLeaveEntitlement] = useState(0);

    /**
     * Function to handle the case that the user clicks on the close button in the modal.
     */
    const handleStatisticsClose = () => props.setShowStatisticsModal(false);

    /**
     * Load the statistics from the REST API based on the supplied information.
     */
    useEffect(() => {
        // Load the statistics for the current year.
        let startYearDate = '01-01-' + props.year;
        let endYearDate = '31-12-' + props.year;
        let username;
        if ( props.username === '') {
            username = props.token.split("-")[0];
        } else {
            username = props.username;
        }
        axios.get('http://localhost:8150/api/absences/?company=' + props.company + '&username=' + username + '&startDate=' + startYearDate + '&endDate=' + endYearDate + '&onlyCount=false&token=' + props.token)
            .then(res => {
                const result = res.data;
                setStatisticsMap(result['statisticsMap']);
            })
        // Get the leave entitlement for this user.
        axios.get('http://localhost:8150/api/user/?company=' + props.company + '&username=' + username + '&token=' + props.token)
            .then(res => {
                const result = res.data;
                setLeaveEntitlement(result['leaveEntitlementPerYear']);
            })
    }, [props.company, props.token, props.year, props.username]);

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Modal show={props.showStatisticsModal} onHide={handleStatisticsClose}>
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
