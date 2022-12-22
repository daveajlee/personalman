import {Container} from "react-bootstrap";

const Footer = () => (
    <div className="Footer">
        <footer className="App-footer mt-5">
            <Container className="bg-light" fluid>
                <p className="text-center">This website uses images from the Unsplash collection and supplied using the <a href="https://unsplash.com/license">Unsplash licence</a>. A list
                    of these images can be provided upon request.</p>
            </Container>
        </footer>
    </div>
)

export default Footer;