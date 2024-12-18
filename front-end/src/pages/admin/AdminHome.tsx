import {
  Button,
  Card,
  Col,
  Container,
  Form,
  InputGroup,
  Row,
} from "react-bootstrap";
import { useEffect, useState } from "react";
import AdminNavBar from "../../components/AdminNavBar";
import axiosInstance from "../../api/axiosInstance";
import { useNavigate } from "react-router-dom";

/**
 * AdminHome component that displays a list of products for the admin user.
 * It allows the admin to search products and remove them from the platform.
 * Products are fetched from the backend API, and the admin can search or filter products by name.
 * 
 * @component
 * @example
 * return (
 *   <AdminHome />
 * )
 */

interface Product {
  id: number;
  name: string;
  category: string;
  description: string;
  price: number;
  imageBase64: string;
}

const AdminHome = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  const getProducts = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      console.log("Cannot access until user logs in");
      navigate("/login");
    } else {
      try {
        console.log("Fetching products...");
        const response = await axiosInstance.get<Product[]>(
          `/api/products/get-all`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setProducts(response.data);
      } catch (error) {
        console.log(error);
      }
    }
  };

  useEffect(() => {
    getProducts();
  }, []);

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
  };

  const handleSearch = async () => {
    console.log("Searching...");
    try {
      const token = localStorage.getItem("token");
      const response = await axiosInstance.get<Product[]>(
        `/api/products/search/${search}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setProducts(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const handleRemove = async (productId: number) => {
    console.log("Removing product...");
    try {
      const token = localStorage.getItem("token");
      const response = await axiosInstance.delete(
        `/api/products/remove/${productId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setProducts((prevProducts) =>
        prevProducts.filter((p) => p.id !== productId)
      );
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <AdminNavBar />
      <Container>
        <div className="search-bar">
          <InputGroup className="mb-3">
            <Form.Control
              placeholder="search"
              aria-label="search-bar"
              aria-describedby="search-bar"
              value={search}
              onChange={handleSearchChange}
            />
            <Button
              variant="outline-secondary"
              id="search-bar-btn"
              onClick={handleSearch}
            >
              Search
            </Button>
          </InputGroup>
        </div>
        <Row className="g-4">
          {products.map((product) => (
            <Col key={product.id}>
              <Card style={{ width: "18rem" }}>
                <Card.Img
                  className="product-image"
                  variant="top"
                  src={product.imageBase64}
                />
                <Card.Body className="home-card-body">
                  <Card.Title>{product.name}</Card.Title>
                  <Card.Text>${product.price}</Card.Text>
                  <Button
                    variant="primary"
                    onClick={() => handleRemove(product.id)}
                  >
                    Remove
                  </Button>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </>
  );
};

export default AdminHome;
