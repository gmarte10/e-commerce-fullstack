import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import NavBar from "../components/NavBar";
import { useState } from "react";
import axiosInstance from "../api/axiosInstance";

interface Product {
  id: number;
  name: string;
  price: number;
  category: string;
  description: string;
  imageBase64: string;
}

const Product = () => {
  const [quantity, setQuantity] = useState(1);
  const navigate = useNavigate();
  const location = useLocation();
  const product: Product = location.state?.product;

  const handleAddToCart = async () => {
    try {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      console.log(`Email: ${email}`);
      console.log(`Token: ${token}`);
      const requestBody = {
          email: email,
          productId: product.id,
          quantity: quantity,
      };
      const response = await axiosInstance.post(
        `/api/cart-items/add`,
        requestBody,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(`CartItem Added: ${JSON.stringify(response.data)}`);
      navigate("/cart");
    } catch (error) {
      console.error(error);
      return "issue adding cart item";
    }
  };

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
              <h1>{product.name}</h1>
              <p>{product.description}</p>
              <p>
                <strong>Category:</strong> {product.category}
              </p>
            </Col>
            <Col md={3} className="text-center">
              <h2>${product.price}</h2>
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
