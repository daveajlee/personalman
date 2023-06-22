import renderer from 'react-test-renderer';
import React from "react";
import Logout from "../pages/Logout"
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import App from "../App";
import ErrorPage from "./ErrorPage";

it('checks that the Logout page can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "/",
                element: <App/>,
                errorElement: <ErrorPage />,
            }])}>
                <Logout/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
