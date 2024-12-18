import { Container, Nav, Navbar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

/**
 * AdminNavBar component that renders the navigation bar for the admin panel.
 * This component provides navigation links for the admin to access different parts of the admin dashboard
 * and logout functionality.
 * 
 * @component
 * @example
 * return (
 *   <AdminNavBar />
 * )
 */

const AdminNavBar = () => {
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

  const handleAddProduct = () => {
    navigate("/add-product");
  };
  return (
    <>
      <div>
        <Navbar expand="lg" className="bg-body-tertiary">
          <Container>
            <Navbar.Brand>Admin</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                <Nav.Link href="/admin-home">Home</Nav.Link>
                <Nav.Link onClick={handleAddProduct} href="/add-product">
                  Add Product
                </Nav.Link>
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

export default AdminNavBar;
