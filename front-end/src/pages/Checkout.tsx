import { Button, Col, Container, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const Checkout = () => {
  const navigate = useNavigate();
  const handlePlaceOrder = () => {
    navigate("/order");
  };

  return (
    <>
      <Container className="checkout-container">
        <Row>
          <Col>
            <Row className="checkout-row">
              <h2>Address</h2>
              <p>Sed ut perspiciatis unde omnis iste natus error</p>
            </Row>
            <Row className="checkout-row">
              <h2>Payment</h2>
              <p>Sed ut perspiciatis unde omnis iste natus error</p>
            </Row>
          </Col>
          <Col className="checkout-col">
            <div className="checkout-col-content">
              <h2>Order Total</h2>
              <p>$100</p>
              <Button onClick = {handlePlaceOrder}>Place Order</Button>
            </div>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Checkout;
