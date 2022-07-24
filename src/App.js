import 'bootstrap/dist/css/bootstrap.min.css';
import './css/App.css'
import {Badge} from "react-bootstrap";
import Header from "./components/Header";

function App() {
  return (
    <div class={"container-fluid"}>
      <Header></Header>
      <h1>Welcome to PersonalMan Web Client <Badge bg="secondary">New</Badge></h1>
    </div>
  );
}

export default App;
