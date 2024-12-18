import {
  Button,
  Card,
  Col,
  Container,
  Form,
  InputGroup,
  Row,
} from "react-bootstrap";

import NavBar from "../components/NavBar";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axiosInstance from "../api/axiosInstance";

interface Product {
  id: number;
  name: string;
  category: string;
  description: string;
  price: number;
  imageBase64: string;
}

const Home = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [search, setSearch] = useState("");
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  const getProducts = async () => {
    if (token === null) {
      console.log("Cannot access until user logs in")
      navigate("/login");
    } else {
      try {
        console.log("Fetching all products...")
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

  const handleItemClick = (product: Product) => {
    navigate(`/product`, { state: { product } });
  };

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
  };

  const handleSearch = async () => {
    try {
      console.log("Searching...")
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

  return (
    <>
      <NavBar />
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
          {products.map((product, index) => (
            <Col key={index}>
              <Card
                style={{ width: "18rem" }}
                onClick={() => handleItemClick(product)}
              >
                <Card.Img
                  className="product-image"
                  variant="top"
                  src={product.imageBase64}
                />
                <Card.Body className="home-card-body">
                  <Card.Title>{product.name}</Card.Title>
                  <Card.Text>${product.price}</Card.Text>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </>
  );
};

export default Home;
