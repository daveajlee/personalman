import renderer from 'react-test-renderer';
import React from "react";
import Login from "../pages/Login"
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import AbsenceManagement from "./AbsenceManagement";
import App from "../App";
import ErrorPage from "./ErrorPage";

it('checks that the Login page can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "/",
                element: <App/>,
                errorElement: <ErrorPage />,
            },{
                path: "/absences",
                element: <AbsenceManagement/>
            }])}>
                <Login/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
