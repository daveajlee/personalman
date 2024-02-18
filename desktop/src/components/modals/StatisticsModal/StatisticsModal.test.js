import renderer from 'react-test-renderer';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import React from "react";
import AbsenceManagement from "../../pages/AbsenceManagement/AbsenceManagement";
import AllAbsences from "../../pages/AllAbsences/AllAbsences";
import App from "../../../App";
import ErrorPage from "../../pages/ErrorPage/ErrorPage";
import StatisticsModal from "./StatisticsModal";

it('checks that the StatisticsModal component can be rendered', () => {

    const component = renderer.create(
        <React.StrictMode>
            <RouterProvider router={createBrowserRouter([{
                path: "*",
                element: <App/>,
                errorElement: <ErrorPage />,
            },{
                path: "/absences",
                element: <AbsenceManagement/>
            },{
                path: "/allAbsences",
                element: <AllAbsences/>
            }])}>
            <StatisticsModal company="Mustermann GmbH" setShowStatisticsModal={() => {}} username="mmustermann" token="mmustermann-ghgkg" year={2013}/>
            </RouterProvider>
        </React.StrictMode>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
