import renderer from 'react-test-renderer';
import React from "react";
import RegisterUser from "./RegisterUser";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import App from "../../../App";
import ErrorPage from "../ErrorPage/ErrorPage";

it('checks that the RegisterUser page can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "/",
                element: <App/>,
                errorElement: <ErrorPage />,
            }])}>
                <RegisterUser/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
