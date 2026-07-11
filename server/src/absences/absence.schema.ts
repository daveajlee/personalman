import mongoose from "mongoose";

export const AbsenceSchema = new mongoose.Schema({
  category: String,
  company: String,
  username: String,
  startDate: String,
  endDate: String
});