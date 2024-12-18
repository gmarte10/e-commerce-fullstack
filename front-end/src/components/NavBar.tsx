import { Container, Nav, Navbar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import '../css/navbar.css';

/**
 * NavBar component that renders the navigation bar for the user interface.
 * This component includes links to the home page, cart, orders, and a logout button for the user.
 * It allows the user to log out and be redirected to the login page, removing user data from `localStorage`.
 * 
 * @component
 * @example
 * return (
 *   <NavBar />
 * )
 */

const NavBar = () => {
  const navigate = useNavigate();
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("email");
    localStorage.removeItem("name");
    localStorage.removeItem("phone");
    localStorage.removeItem("address");
    localStorage.removeItem("role");
    console.log("Logging out");
    navigate("/login");
  };
  return (
    <>
      <div>
        <Navbar expand="lg" className="bg-body-tertiary">
          <Container className="container-navbar">
            <Navbar.Brand>E-Commerce</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                <Nav.Link href="/home">Home</Nav.Link>
                <Nav.Link href="/cart">Cart</Nav.Link>
                <Nav.Link href="/order">Orders</Nav.Link>
                <Nav.Link onClick={handleLogout} href="/login">
                  Logout
                </Nav.Link>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
      </div>
    </>
  );
};

export default NavBar;
