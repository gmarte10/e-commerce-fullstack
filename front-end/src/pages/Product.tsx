import { Button, Card, Col, Container, Row } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import NavBar from "../components/NavBar";

interface Product {
  id: number;
  name: string;
  price: number;
  category: string;
  description: string;
  imageBase64: string;
}

const Product = () => {
  const navigate = useNavigate();
  const handleAddToCart = () => {
    
    navigate("/cart");
  };
  const location = useLocation();
  const product: Product = location.state?.product;

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
