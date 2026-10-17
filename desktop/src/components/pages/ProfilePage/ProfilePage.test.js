import renderer from 'react-test-renderer';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import React from "react";
import App from "../../../App";
import ErrorPage from "../ErrorPage/ErrorPage";
import ProfilePage from "../ProfilePage/ProfilePage";

it('checks that the Profile page can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "*",
                element: <App/>,
                errorElement: <ErrorPage />,
            },{
                path: "/profile",
                element: <ProfilePage/>
            },])}>
            <AbsenceManagement/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
