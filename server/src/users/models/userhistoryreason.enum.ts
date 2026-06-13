export const UserHistoryReason = {

    /**
     * User joins the company.
     */
    JOINED: {
        /**
         * Return the text of joined.
         * @return a <code>String</code> object representing the text for joined.
         */
        getText: function() {
            return "Joined";
        }
    },

    /**
     * User has been paid.
     */
    PAID: {
        /**
         * Return the text for paid.
         * @return a <code>String</code> object representing the text for paid.
         */
        getText: function() {
            return "Paid";
        }
    },

    /**
     * User has been given their annual evaluation.
     */
    EVALUATED: {
        /**
         * Return the text for evaluated.
         * @return a <code>String</code> object representing the text for evaluated.
         */
        getText: function() {
            return "Evaluated";
        }
    },

    /**
     * User has been given a warning.
     */
    WARNED: {
        /**
         * Return the text for warned.
         * @return a <code>String</code> object representing the text for warned.
         */
        getText: function() {
            return "Warned";
        }
    },

    /**
     * User has resigned from the company on their own wish.
     */
    RESIGNED: {
        /**
         * Return the text for resigned.
         * @return a <code>String</code> object representing the text for resigned.
         */
        getText: function() {
            return "Resigned";
        }
    },

    /**
     * User has been sacked.
     */
    SACKED: {
        /**
         * Return the text for sacked.
         * @return a <code>String</code> object representing the text for sacked.
         */
        getText: function() {
            return "Sacked";
        }
    }
};