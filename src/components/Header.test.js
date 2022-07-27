import {render, screen} from '@testing-library/react';
import Header from "./Header";
import {MemoryRouter} from "react-router-dom";

test('renders profile link', () => {
    render(<MemoryRouter><Header /></MemoryRouter>);
    //Check that logo is shown.
    const displayedImage = screen.getByAltText("PersonalMan Logo");
    expect(displayedImage.src).toContain("personalmanlogo-icon.png");
    // Check that profile link is shown.
    const profileText = screen.getByText("Profile")
    expect(profileText).toBeInTheDocument();
    //Check that absences link is shown.
    const absencesText = screen.getByText("Absences")
    expect(absencesText).toBeInTheDocument();
    //Check that logout link is shown.
    const logoutText = screen.getByText("Logout")
    expect(logoutText).toBeInTheDocument();
});
