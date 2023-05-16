import 'bootstrap/dist/css/bootstrap.min.css';
import './css/App.css'
import Login from "./pages/Login";
import Register from "./pages/Register";
import {useEffect, useState} from "react";
import axios from "axios";


function App() {

  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get(`http://localhost:8150/api/companies/`)
        .then(res => {
          setLoading(false);
          const companies = res.data;
          setCompanies(companies);
        })
  }, []);

  return (
      <div>
        {!loading && companies && companies.length > 0 ? <Login /> : <Register />
        }
      </div>
  );
}

export default App;
