import renderer from 'react-test-renderer';
import React from "react";
import App from "./App";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import ErrorPage from "./pages/ErrorPage";

it('checks that the App component can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "/*",
                element: <App/>,
                errorElement: <ErrorPage />,
            }])}>
                <App/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
