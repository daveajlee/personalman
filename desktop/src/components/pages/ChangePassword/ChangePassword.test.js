import renderer from 'react-test-renderer';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import React from "react";
import ChangePassword from "./ChangePassword";
import App from "../../../App";
import ErrorPage from "../ErrorPage/ErrorPage";

it('checks that the ChangePassword page can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "*",
                element: <App/>,
                errorElement: <ErrorPage />,
            }])}>
            <ChangePassword/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
