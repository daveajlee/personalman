import 'bootstrap/dist/css/bootstrap.min.css';
import './css/App.css'
import Login from "./pages/Login";
import RegisterCompany from "./pages/RegisterCompany";
import {useEffect, useState} from "react";
import axios from "axios";

/**
 * This is the first page that the user visits when starting PersonalMan - either show the login screen or the register
 * a company screen if no companies have been defined so far.
 * @returns {JSX.Element} to be displayed to the user.
 */
function App() {

  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);

    /**
     * Load the current list of companies that are available on the REST API server.
     */
  useEffect(() => {
    axios.get(process.env.REACT_APP_SERVER_URL + `/companies/`)
        .then(res => {
          setLoading(false);
          const companies = res.data;
          setCompanies(companies);
        })
  }, []);

    /**
     * Display the relevant elements and data to the user.
     */
    return (
      <div>
        {!loading && companies && companies.length > 0 ? <Login /> : <RegisterCompany />
        }
      </div>
  );
}

export default App;
