import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import NavBar from "../components/NavBar";
import { useEffect, useState } from "react";
import axiosInstance from "../api/axiosInstance";
import "../css/product.css";

interface Product {
  id: number;
  name: string;
  category: string;
  description: string;
  price: number;
  imageBase64: string;
}

const Product = () => {
  const [quantity, setQuantity] = useState(1);
  const navigate = useNavigate();
  const location = useLocation();
  const product: Product = location.state?.product;

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      console.log("Cannot access until user logs in");
      navigate("/login");
    }
  }, [navigate]);

  const handleAddToCart = async () => {
    try {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      const requestBody = {
        email: email,
        productId: product.id,
        quantity: quantity,
      };
      await axiosInstance.post(`/api/cart-items/create`, requestBody, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log(`Product added to cart successfully`);
      navigate("/cart");
    } catch (error) {
      console.log(error);
    }
  };

  if (!product) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <NavBar />
      <div className="product-detail">
        <Container>
          <Row className="align-item-center">
            <Col md={4}>
              <img
                className="product-detail-img"
                src={product.imageBase64}
                alt="product"
              />
            </Col>
            <Col md={5}>
              <h1 id="product-name">{product.name}</h1>
              <p className="product-text">{product.description}</p>
              <p className="product-text">
                <strong>Category:</strong> {product.category}
              </p>
            </Col>
            <Col md={3} className="text-center">
              <h2 id="product-price">${product.price}</h2>
              <Form.Group controlId="quantity" className="mt-3">
                <Form.Label>Quantity</Form.Label>
                <Form.Control
                  as="select"
                  value={quantity}
                  onChange={(e) => setQuantity(Number(e.target.value))}
                >
                  {[...Array(5).keys()].map((i) => (
                    <option key={i} value={i + 1}>
                      {i + 1}
                    </option>
                  ))}
                </Form.Control>
              </Form.Group>
              <Button
                onClick={handleAddToCart}
                variant="primary"
                className="mt-3"
              >
                Add To Cart
              </Button>
            </Col>
          </Row>
        </Container>
      </div>
    </>
  );
};

export default Product;
