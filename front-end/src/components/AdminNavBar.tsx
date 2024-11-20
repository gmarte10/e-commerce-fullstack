import { Container, Nav, Navbar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const AdminNavBar = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
      // localStorage.removeItem('token');
      navigate("/login");
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
                  <Nav.Link href="/add-product">Add Product</Nav.Link>
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
}
 
export default AdminNavBar;