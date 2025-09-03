import {Button, Modal} from "react-bootstrap";
import {useEffect, useState} from "react";
import axios from "axios";
import PropTypes from "prop-types";
import {useTranslation} from "react-i18next";
import * as React from "react";

type StatisticsModalProps = {
    setShowStatisticsModal: Function;
    year: number;
    company: string;
    username: string;
    token: string;
    showStatisticsModal: boolean;
}

/**
 * This is the modal window to show the view statistics for a particular user. The modal is used for admin users who want
 * to view the statistics of a particular user.
 * @param props setShowStatisticsModal - function to determine whether modal should be shown or not,
 * company - the company that the user belongs to, username - the username of the user that the statistics should be displayed for,
 * token - the current user access token of the admin user, year - the year that the statistics should be displayed for
 * @returns {JSX.Element} to be displayed to the user.
 */
function StatisticsModal ({setShowStatisticsModal, year, company, username, token, showStatisticsModal}: StatisticsModalProps): React.JSX.Element {

    const [statisticsMap, setStatisticsMap] = useState<{[key: string]: string}>({});
    const [leaveEntitlement, setLeaveEntitlement] = useState(0);

    const {t} = useTranslation();

    /**
     * Function to handle the case that the user clicks on the close button in the modal.
     */
    const handleStatisticsClose = () => setShowStatisticsModal(false);

    /**
     * Load the statistics from the REST API based on the supplied information.
     */
    useEffect(() => {
        // Load the statistics for the current year.
        let startYearDate = '01-01-' + year;
        let endYearDate = '31-12-' + year;
        if ( username === '') {
            username = token.split("-")[0];
        }
        axios.get(import.meta.env.REACT_APP_SERVER_URL + '/absences/?company=' + company + '&username=' + username + '&startDate=' + startYearDate + '&endDate=' + endYearDate + '&onlyCount=false&token=' + token)
            .then(res => {
                const result = res.data;
                setStatisticsMap(result['statisticsMap']);
            }).catch(error => {
                console.error(error);
        })
        // Get the leave entitlement for this user.
        axios.get(import.meta.env.REACT_APP_SERVER_URL  + '/user/?company=' + company + '&username=' + username + '&token=' + token)
            .then(res => {
                const result = res.data;
                setLeaveEntitlement(result['leaveEntitlementPerYear']);
            }).catch(error => {
                console.error(error);
        })
    }, [company, token, year, username]);

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Modal show={showStatisticsModal} onHide={handleStatisticsClose}>
            <Modal.Header closeButton>
                <Modal.Title>{t('statisticsModalTitle')} - {username} - {year}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{t('Illness')}: {statisticsMap['Illness']} {t('statisticsModalDays')} <br/> {t('Holiday')}: {statisticsMap['Holiday']} {t('statisticsModalDays')} ({t('statisticsModalRemaining')}: {leaveEntitlement} {t('statisticsModalDays')}) <br/>
                {t('Trip')}: {statisticsMap['Trip']} {t('statisticsModalDays')} <br/> {t('Conference')}: {statisticsMap['Conference']} {t('statisticsModalDays')} <br/> {t('DayinLieu')}: {statisticsMap['Day in Lieu']} {t('statisticsModalDays')} ({t('statisticsModalRemaining')}: {statisticsMap['Day in Lieu Request']} {t('statisticsModalDays')}) <br/>
                {t('FederalHoliday')}: {statisticsMap['Federal Holiday']} {t('statisticsModalDays')}</Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={handleStatisticsClose}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
    );

}

export default StatisticsModal;

StatisticsModal.propTypes = {
    /**
     * the company that the user belongs to
     */
    company: PropTypes.string,

    /**
     * function to determine whether modal should be shown or not
     */
    setShowStatisticsModal: PropTypes.func,

    /**
     * the username of the user that the statistics should be displayed for
     */
    username: PropTypes.string,

    /**
     * the current user access token of the admin user
     */
    token: PropTypes.string,

    /**
     * the year that the statistics should be displayed for
     */
    year: PropTypes.number
}

    StatisticsModal.defaultProps = {
    company: 'Required',
    username: 'Required',
    token: 'Required'
};
