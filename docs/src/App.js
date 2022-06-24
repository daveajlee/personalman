import logo from './personalman-logo.png';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {Navbar} from "react-bootstrap";
import {Container} from "react-bootstrap";
import {Nav} from "react-bootstrap";
import Home from "./pages/Home";
import Vision from "./pages/Vision";
import Features from "./pages/Features";
import Roadmap from "./pages/Roadmap";
import Architecture from "./pages/Architecture";
import Api from "./pages/Api";
import Download from "./pages/Download";
import {Route} from "react-router-dom";
import {Routes} from "react-router-dom";
import { Link } from 'react-router-dom';

function Header() {
  return ( <div className="Header">
    <header className="App-header">
        <Navbar bg="light" expand="lg">
            <Container>
                <Navbar.Brand as={Link} to="/"><img src={logo} alt="PersonalMan"
                                                className="img-responsive img-max-height"/></Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/">Home</Nav.Link>
                        <Nav.Link as={Link} to="/vision">Vision</Nav.Link>
                        <Nav.Link as={Link} to="/features">Features</Nav.Link>
                        <Nav.Link as={Link} to="/roadmap">Roadmap</Nav.Link>
                        <Nav.Link as={Link} to="/architecture">Architecture</Nav.Link>
                        <Nav.Link as={Link} to="/api">API</Nav.Link>
                        <Nav.Link as={Link} to="/download">Download</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    </header>
  </div>);
}

function Footer() {
    return ( <div className="Footer">
        <footer className="App-footer mt-5">
            <Container className="bg-light" fluid>
                <p className="text-center">This website uses images from the Unsplash collection and supplied using the <a href="https://unsplash.com/license">Unsplash licence</a>. A list
                    of these images can be provided upon request.</p>
            </Container>
        </footer>
    </div>);
}

function App() {
  return (
    <div className="App">
            <Header />
            <div className="container mt-2" style={{ marginTop: 40 }}>
                <Routes>
                    <Route exact path='/' element={<Home />} />
                    <Route path='/vision' element={<Vision />} />
                    <Route path='/features' element={<Features />} />
                    <Route path='/roadmap' element={<Roadmap />} />
                    <Route path='/architecture' element={<Architecture />} />
                    <Route path='/api' element={<Api />} />
                    <Route path='/download' element={<Download />} />
                </Routes>
            </div>
        <Footer />
    </div>
  );
}

export default App;
