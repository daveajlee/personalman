import renderer from 'react-test-renderer';
import React from "react";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import RegisterUser from "./RegisterUser";
import RegisterCompany from "./RegisterCompany";
import App from "../App";
import ErrorPage from "./ErrorPage";

it('checks that the RegisterCompany page can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "/",
                element: <App/>,
                errorElement: <ErrorPage />,
            },{
                path: "/registerUser",
                element: <RegisterUser/>,
            }])}>
                <RegisterCompany/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
