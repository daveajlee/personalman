import renderer from 'react-test-renderer';
import AbsenceList from "./AbsenceList";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import React from "react";
import AbsenceManagement from "../pages/AbsenceManagement";
import AllAbsences from "../pages/AllAbsences";
import App from "../App";
import ErrorPage from "../pages/ErrorPage";

it('checks that the AbsenceList component can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "/",
                element: <App/>,
                errorElement: <ErrorPage />,
            },{
                path: "/absences",
                element: <AbsenceManagement/>
            },{
                path: "/allAbsences",
                element: <AllAbsences/>
            }])}>
            <AbsenceList company="Mustermann GmbH" username="mmustermann" startDate="01-06-2023" endDate="30-06-2023"
                token="mmustermann-ghgkg" month={6} year={2023}/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
