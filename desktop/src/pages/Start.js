import React from "react";
import Header from "../components/Header";
import AbsenceManagement from "./AbsenceManagement";
import {Routes} from "react-router-dom";
import {Route} from "react-router-dom";
import Logout from "./Logout";
import Profile from "./Profile";

const Start = () => (

    <div className={"container-fluid"}>

        <Header></Header>

        <div className="container mt-2" style={{ marginTop: 40 }}>
            <Routes>
                <Route exact path='/profile' element={<Profile />} />
                <Route path='/absences' element={<AbsenceManagement />} />
                <Route path='/logout' element={<Logout />} />
            </Routes>
        </div>

    </div>

);

export default Start;
