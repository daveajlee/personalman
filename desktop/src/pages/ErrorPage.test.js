import renderer from 'react-test-renderer';
import React from "react";
import ErrorPage from "../pages/ErrorPage";

it('checks that the ErrorPage page can be rendered', () => {

    const component = renderer.create(
        <ErrorPage/>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
});
