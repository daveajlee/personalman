import renderer from 'react-test-renderer';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import React from "react";
import AbsenceManagement from "./AbsenceManagement";
import AllAbsences from "../AllAbsences/AllAbsences";
import App from "../../../App";
import ErrorPage from "../ErrorPage/ErrorPage";

it('checks that the AbsenceManagement page can be rendered', () => {

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
            <AbsenceManagement/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
