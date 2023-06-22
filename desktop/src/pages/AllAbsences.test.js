import renderer from 'react-test-renderer';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import React from "react";
import AbsenceManagement from "../pages/AbsenceManagement";
import AllAbsences from "../pages/AllAbsences";
import App from "../App";
import ErrorPage from "../pages/ErrorPage";

it('checks that the AllAbsences page can be rendered', () => {

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
            <AllAbsences/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
