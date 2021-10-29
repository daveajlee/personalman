package de.davelee.trams.operations.model;

/**
 * This class represents the type of a vehicle as a simple enum which can be returned as part of the vehicle response.
 * @author Dave Lee
 */
public enum VehicleType {

    BUS {
        /**
         * Return the name of the type as a string e.g. Bus
         * @return a <code>String</code> containing the name of the type.
         */
        @Override
        public String getTypeName() {
            return "Bus";
        }

        /**
         * Return the number of years between inspections.
         * @return a <code>int</code> containing the number of years between inspections.
         */
        @Override
        public int getInspectionPeriod() {
            return 3;
        }

        /**
         * Return the maximum number of hours that a vehicle should be in service before taking a break.
         */
        @Override
        public int getMaximumHoursPerDay() {
            return 16;
        }

    },

    TRAIN {
        /**
         * Return the name of the type as a string e.g. Train
         * @return a <code>String</code> containing the name of the type.
         */
        @Override
        public String getTypeName() {
            return "Train";
        }

        /**
         * Return the number of years between inspections.
         * @return a <code>int</code> containing the number of years between inspections.
         */
        @Override
        public int getInspectionPeriod() {
            return 8;
        }

        /**
         * Return the maximum number of hours that a vehicle should be in service before taking a break.
         */
        @Override
        public int getMaximumHoursPerDay() {
            return 21;
        }
    },

    TRAM {
        /**
         * Return the name of the type as a string e.g. Tram
         * @return a <code>String</code> containing the name of the type.
         */
        @Override
        public String getTypeName() {
            return "Tram";
        }

        /**
         * Return the number of years between inspections.
         * @return a <code>int</code> containing the number of years between inspections.
         */
        @Override
        public int getInspectionPeriod() {
            return 9;
        }

        /**
         * Return the maximum number of hours that a vehicle should be in service before taking a break.
         */
        @Override
        public int getMaximumHoursPerDay() {
            return 20;
        }
    };

    /**
     * Return the name of the type as a string e.g. Bus
     * @return a <code>String</code> containing the name of the type.
     */
    public abstract String getTypeName();

    /**
     * Return the number of years between inspections.
     * @return a <code>int</code> containing the number of years between inspections.
     */
    public abstract int getInspectionPeriod();

    /**
     * Return the maximum number of hours that a vehicle should be in service before taking a break.
     */
    public abstract int getMaximumHoursPerDay();

}
