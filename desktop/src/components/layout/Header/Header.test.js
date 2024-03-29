import renderer from 'react-test-renderer';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import React from "react";
import AbsenceManagement from "../../pages/AbsenceManagement/AbsenceManagement";
import AllAbsences from "../../pages/AllAbsences/AllAbsences";
import App from "../../../App";
import ErrorPage from "../../pages/ErrorPage/ErrorPage";
import Header from "./Header";

it('checks that the Header component can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "*",
                element: <App/>,
                errorElement: <ErrorPage />,
            },{
                path: "/absences",
                element: <AbsenceManagement/>
            },{
                path: "/allAbsences",
                element: <AllAbsences/>
            }])}>
            <Header company="Mustermann GmbH" token="mmustermann-ghgkg"/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
