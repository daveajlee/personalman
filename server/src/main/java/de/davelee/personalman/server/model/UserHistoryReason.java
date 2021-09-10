package de.davelee.personalman.server.model;

/**
 * This enum contains the various allowed reasons for something to be written into the user's history.
 * @author Dave Lee
 */
public enum UserHistoryReason {

    /**
     * User joins the company.
     */
    JOINED {
        /**
         * Return the text of joined.
         * @return a <code>String</code> object representing the text for joined.
         */
        public String getText() {
            return "Joined";
        }
    },

    /**
     * User has been paid.
     */
    PAID {
        /**
         * Return the text for paid.
         * @return a <code>String</code> object representing the text for paid.
         */
        public String getText() { return "Paid"; }
    },

    /**
     * User has been given their annual evaluation.
     */
    EVALUATED {
        /**
         * Return the text for evaluated.
         * @return a <code>String</code> object representing the text for evaluated.
         */
        public String getText() { return "Evaluated"; }
    },

    /**
     * User has been given a warning.
     */
    WARNED {
        /**
         * Return the text for warned.
         * @return a <code>String</code> object representing the text for warned.
         */
        public String getText() { return "Warned"; }
    },

    /**
     * User has resigned from the company on their own wish.
     */
    RESIGNED {
        /**
         * Return the text for resigned.
         * @return a <code>String</code> object representing the text for resigned.
         */
        public String getText() { return "Resigned"; }
    },

    /**
     * User has been sacked.
     */
    SACKED {
        /**
         * Return the text for sacked.
         * @return a <code>String</code> object representing the text for sacked.
         */
        public String getText() { return "Sacked"; }
    };

    /**
     * Abstract method to return the text for a particular reason.
     * @return a <code>String</code> object representing the text for a particular reason.
     */
    public abstract String getText();

}
