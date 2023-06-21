import renderer from 'react-test-renderer';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import React from "react";
import AbsenceManagement from "../pages/AbsenceManagement";
import AllAbsences from "../pages/AllAbsences";
import App from "../App";
import ErrorPage from "../pages/ErrorPage";
import AddUserModal from "./AddUserModal";

it('checks that the AddUserModal component can be rendered', () => {

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
            <AddUserModal company="Mustermann GmbH" setShowAddUserModal={() => {}}/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
