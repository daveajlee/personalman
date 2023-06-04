import React from 'react';
import ReactDOM from 'react-dom/client';
import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";
import './index.css';
import App from './App';
import ErrorPage from "./pages/ErrorPage";
import RegisterCompany from "./pages/RegisterCompany";
import RegisterUser from "./pages/RegisterUser";
import reportWebVitals from './reportWebVitals';
import AbsenceManagement from "./pages/AbsenceManagement";
import ChangePassword from "./pages/ChangePassword";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>,
        errorElement: <ErrorPage />,
    },
    {
        path: "/registerCompany",
        element: <RegisterCompany/>
    },
    {
        path: "/registerUser",
        element: <RegisterUser/>
    },
    {
        path: "/absences",
        element: <AbsenceManagement/>
    },
    {
        path: "/changePassword",
        element: <ChangePassword/>
    }
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
      <RouterProvider router={router} />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
