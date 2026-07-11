import mongoose from "mongoose";
export const UserSchema = new mongoose.Schema({
    firstName: String,
    lastName: String,
    userName: String,
    password: String,
    company: String,
    leaveEntitlementPerYear: Number,
    workingDays: [String],
    position: String,
    startDate: Date,
    endDate: Date,
    accountStatus: String,
    dateOfBirth: Date,
    role: String,
    hourlyWage: Number,
    contractedHoursPerWeek: Number,
    trainingsList: [String],
    timesheet: Map<Date, Number>,
    userHistoryEntryList: [{
        date: Date,
        reason: String,
        comment: String
    }]
});