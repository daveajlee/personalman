import { UserAccountStatus } from "./useraccountstatus.enum";

export class User {

    /**
     * The first name of this user.
     */
    private firstName: string;

    /**
     * The surname of this user.
     */
    private lastName: string;

    /**
     * The username for this user.
     */
    private userName: string;

    /**
     * The password for this user.
     */
    private password: string;

    /**
     * The company that the user works for.
     */
    private company: string;

    /**
     * The leave entitlement of the person in days per year.
     */
    private leaveEntitlementPerYear: number;

    /**
     * The names of the working days that the user is normally expected to work.
     */
    private workingDays: DayOfWeek[];

    /**
     * The current job title of the user.
     */
    private position: string;

    /**
     * The date that the person started at the company.
     */
    private startDate: LocalDate;

    /**
     * The date that the person stopped working for the company.
     */
    private endDate: LocalDate;

    /**
     * The status of this user's account.
     */
    private accountStatus: UserAccountStatus;

    /**
     * The person's date of birth.
     */
    private dateOfBirth: LocalDate;

    /**
     * The role that the user has in PersonalMan for this company.
     */
    private role: string;

    /**
     * The hourly wage that the user is paid.
     */
    private hourlyWage: BigDecimal;

    /**
     * The number of contracted hours that the user works per week.
     */
    private contractedHoursPerWeek: number;

    /**
     * Any trainings or qualifications that the user has.
     */
    private trainingsList: string[];

    /**
     * The number of hours that a user has worked on a particular day.
     */
    private timesheet: Map<LocalDate, number>;

    /**
     * A log of entries representing the history of this user whilst working for this company.
     */
    private userHistoryEntryList: UserHistoryEntry[];

    /**
     * Add a new training to the list.
     * @param trainingCourse a <code>String</code> containing the number of the training course or qualification.
     */
    public addTrainingCourse ( trainingCourse: string ): void {
        this.trainingsList.push(trainingCourse);
    }

    /**
     * Add a number of hours for a particular day to the timesheet.
     * @param hours a <code>int</code> with the number of hours to add.
     * @param date a <code>LocalDate</code> object containing the day to add the hours to.
     */
    public addHoursForDate ( hours: number, date: LocalDate ) : void {
        //If the date already exists then add the hours to the hours already there.
        if ( timesheet.get(date) != null ) {
            timesheet.put(date, timesheet.get(date).intValue() + hours);
        } else {
            //If no hours are present then just add it as first entry.
            timesheet.put(date, hours);
        }
    }

    /**
     * Retrieve the number of hours that the user has worked on a particular day.
     * @param date a <code>LocalDate</code> object containing the day to retrieve hours for.
     * @return a <code>int</code> with the number of hours.
     */
    public getHoursForDate ( date: LocalDate ) : number {
        //If the date is null then return 0.
        if ( timesheet.get(date) == null ) {
            return 0;
        }
        //Otherwise return the number of hours.
        return timesheet.get(date);
    }

    /**
     * Add a new history entry to the list.
     * @param date a <code>LocalDate</code> containing the date that the entry/event took place.
     * @param userHistoryReason a <code>UserHistoryReason</code> containing the reason that the entry/event took place.
     * @param comment a <code>String</code> containing the comment about the entry/event.
     */
    public addUserHistoryEntry ( date: LocalDate, userHistoryReason: UserHistoryReason, comment: string ) : void {
        if ( userHistoryEntryList == null ) {
            userHistoryEntryList = new ArrayList<>();
        }
        userHistoryEntryList.add(UserHistoryEntry.builder()
                .date(date)
                .userHistoryReason(userHistoryReason)
                .comment(comment)
                .build());
    }

    /**
     * Retrieve the password for this user.
     * @return a <code>String</code> containing the password for this user.
     */
    public getPassword () : string {
        return this.password;
    }

    /**
     * Set the password for this user.
     * @param password a <code>String</code> containing the password for this user.
     */
    public setPassword ( password: string ) : void {
        this.password = password;
    }

    /**
     * Retrieve the user history entry list for this user.
     * @return a <code>array</code> of <code>UserHistoryEntry</code> objects representing the history of this user.
     */
    public getUserHistoryEntryList () : UserHistoryEntry[] {
        return this.userHistoryEntryList;
    }
    
    /**
     * Set the user history entry list for this user.
     * @param userHistoryEntryList a <code>array</code> of <code>UserHistoryEntry</code> objects representing the history of this user.
     */
    public setUserHistoryEntryList ( userHistoryEntryList: UserHistoryEntry[] ) : void {
        this.userHistoryEntryList = userHistoryEntryList;
    }

    /**
     * Retrieve the timesheet for this user.
     * @return a <code>Map</code> of <code>Date</code> to <code>int</code> representing the timesheet for this user.
     */
    public getTimesheet () : Map<Date, number> {
        return this.timesheet;
    }
    
    /**
     * Set the timesheet for this user.
     * @param timesheet a <code>Map</code> of <code>Date</code> to <code>int</code> representing the timesheet for this user.
     */
    public setTimesheet ( timesheet: Map<Date, number> ) : void {
        this.timesheet = timesheet;
    }

    /**
     * Retrieve the trainings list for this user.
     * @return a <code>array</code> of <code>String</code> objects representing the trainings or qualifications that this user has.
     */
    public getTrainingsList () : string[] {
        return this.trainingsList;
    }
    
    /**
     * Set the trainings list for this user.
     * @param trainingsList a <code>array</code> of <code>String</code> objects representing the trainings or qualifications that this user has.
     */
    public setTrainingsList ( trainingsList: string[] ) : void {
        this.trainingsList = trainingsList;
    }

    /**
     * Set the hourly wage for this user.
     * @param hourlyWage a <code>BigDecimal</code> containing the hourly wage for this user.
     */
    public setHourlyWage ( hourlyWage: number ) : void {
        this.hourlyWage = hourlyWage;
    }

    /**
     * Set the contracted hours per week for this user.
     * @param contractedHoursPerWeek a <code>int</code> containing the contracted hours per week for this user.
     */
    public setContractedHoursPerWeek ( contractedHoursPerWeek: number ) : void {
        this.contractedHoursPerWeek = contractedHoursPerWeek;
    }

    /**
     * Get the leave entitlement per year for this user.
     * @return a <code>int</code> containing the leave entitlement per year for this user.
     */
    public getLeaveEntitlementPerYear () : number {
        return this.leaveEntitlementPerYear;
    }

    /**
     * Set the account status for this user.
     * @param accountStatus
     */
    public setAccountStatus(accountStatus: UserAccountStatus) {
        this.accountStatus = accountStatus;
    }

    /**
     * Set the end date for this user.
     */
    public setEndDate(endDate: Date) {
        this.endDate = endDate;
    }

    /**
     * Get the end date for this user.
     */
    public getEndDate() {
        return this.endDate;
    }

    /**
     * Get the contracted hours for this user.
     */
    public getContractedHoursPerWeek() {
        return this.contractedHoursPerWeek;
    }

    /**
     * Get the hourly wage for this user.
     */
    public getHourlyWage() {
        return this.hourlyWage;
    }

    /**
     * Get the date of birth for this user.
     */
    public getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     * Get the role of this user.
     */
    public getRole() {
        return this.role;
    }

    /**
     * Get the start date of this user.
     */
    public getStartDate() {
        return this.startDate;
    }

    /**
     * Get the position of this user.
     */
    public getPosition() {
        return this.position;
    }

    /**
     * Get the first name of this user.
     */
    public getFirstName() {
        return this.firstName;
    }

    /**
     * Get the surname of this user.
     */
    public getLastName() {
        return this.lastName;
    }

    /**
     * Get the username of this user.
     */
    public getUsername() {
        return this.userName;
    }

    /**
     * Get the company for this user.
     */
    public getCompany() {
        return this.company;
    }

}