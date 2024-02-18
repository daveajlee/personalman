import renderer from 'react-test-renderer';
import React from "react";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import App from "../../../App";
import ErrorPage from "../ErrorPage/ErrorPage";
import UsersManagement from "./UsersManagement";

it('checks that the UsersManagement page can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "*",
                element: <App/>,
                errorElement: <ErrorPage />,
            }])}>
                <UsersManagement/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
