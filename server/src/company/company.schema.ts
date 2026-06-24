import mongoose from "mongoose";

export const CompanySchema = new mongoose.Schema({
    name: String,
    defaultAnnualLeaveInDays: Number,
    country: String
});