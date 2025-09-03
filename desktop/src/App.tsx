import 'bootstrap/dist/css/bootstrap.min.css';
import './css/App.css'
import Login from "./components/pages/Login/Login";
import RegisterCompany from "./components/pages/RegisterCompany/RegisterCompany";
import {useEffect, useState} from "react";
import axios from "axios";
import {Route, Routes} from "react-router-dom";
import RegisterUser from "./components/pages/RegisterUser/RegisterUser";
import AbsenceManagement from "./components/pages/AbsenceManagement/AbsenceManagement";
import ChangePassword from "./components/pages/ChangePassword/ChangePassword";
import UsersManagement from "./components/pages/UsersManagement/UsersManagement";
import AllAbsences from "./components/pages/AllAbsences/AllAbsences";
import Logout from "./components/pages/Logout/Logout";
import * as React from "react";

/**
 * This is the first page that the user visits when starting PersonalMan - either show the login screen or the register
 * a company screen if no companies have been defined so far.
 * @returns {Element} to be displayed to the user.
 */
function App(): React.JSX.Element {

  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);

    /**
     * Load the current list of companies that are available on the REST API server.
     */
  useEffect(() => {
    axios.get(import.meta.env.VITE_SERVER_URL + `/companies/`)
        .then(res => {
          setLoading(false);
          const companies = res.data;
          setCompanies(companies);
        }).catch(error => {
            console.log('No connection to API available: ', error);
    })
  }, []);

    /**
     * Display the relevant elements and data to the user.
     */
    return (
        <Routes>
            <Route path="/" element={!loading && companies && companies.length > 0 ? <Login/> : <RegisterCompany/>}/>
            <Route path='/login' element={<Login/>} />
            <Route path='/registerCompany' element={<RegisterCompany/>} />
            <Route path='/registerUser' element={<RegisterUser companyName="" token=""/>} />
            <Route path='/absences' element={<AbsenceManagement docMode="false"/>} />
            <Route path='/changePassword' element={<ChangePassword docMode="false"/>} />
            <Route path='/users' element={<UsersManagement docMode="false"/>} />
            <Route path='/allAbsences' element={<AllAbsences docMode="false"/>} />
            <Route path='/logout' element={<Logout docMode="false"/>} />
        </Routes>
  );
}

export default App
