import mongoose from "mongoose";
import { UserAccountStatus } from "./models/useraccountstatus.enum";
import { UserHistoryEntry } from "./models/userhistory.entry";

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
    //accountStatus: UserAccountStatus,
    dateOfBirth: Date,
    role: String,
    hourlyWage: Number,
    contractedHoursPerWeek: Number,
    trainingsList: [String],
    timesheet: Map<Date, Number>,
    //userHistoryEntryList: [UserHistoryEntry]
});