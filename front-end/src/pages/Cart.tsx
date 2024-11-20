import { Button, Col, Container, ListGroup, Row } from "react-bootstrap";
import sonym5 from "../assets/sonym5.jpg";
import iphone from "../assets/iphone15.jpg";
import celtics from "../assets/celticshoodie.jpg";
import chicken from "../assets/ccsandwich.jpg";
import NavBar from "../components/NavBar";
import { useNavigate } from "react-router-dom";

interface Prod {
  id: number;
  name: string;
  price: number;
  category: string;
  description: string;
  imageBase64: string;
}

interface CartItem {
  id: number;
  name: string;
  price: number;
  category: string;
  description: string;
  imageBase64: string;
  quantity: number;
}

const cartItems: CartItem[] = [
  {
    id: 1,
    name: "Iphone 15",
    price: 900,
    category: "Tech",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: iphone,
    quantity: 2,
  },
  {
    id: 2,
    name: "Sony m5 headphones",
    price: 499.99,
    category: "Tech",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: sonym5,
    quantity: 1,
  },
  {
    id: 3,
    name: "Celtics Hoodie",
    price: 120,
    category: "Clothing",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: celtics,
    quantity: 3,
  },
  {
    id: 4,
    name: "Crispy Chicken Sandwich",
    price: 10,
    category: "Food",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: chicken,
    quantity: 1,
  },
];
const Cart = () => {
  const navigate = useNavigate();
  const handleCheckout = () => {
    navigate("/checkout");
  };
  return (
    <>
      <NavBar />
      <div className="cart">
        <Container>
          <Row>
            <Col md={6}>
              {cartItems.map((cartItem) => (
                <Row className="cart-product" key={cartItem.id}>
                  <Col>
                    <img
                      className="cart-image"
                      src={cartItem.imageBase64}
                      alt="cart image"
                    />
                  </Col>
                  <Col>
                    <div className="cart-item-text">
                      <h5>{cartItem.name}</h5>
                      <div className="cart-item-info">
                        <p>${cartItem.price}</p>
                        <p>Quantity: {cartItem.quantity}</p>
                      </div>
                    </div>
                  </Col>
                  <Col className="cart-col-btn">
                    <div className="cart-div-rm-btn">
                      <Button>Remove</Button>
                    </div>
                  </Col>
                </Row>
              ))}
            </Col>
            <Col md = {6} id="cart-right-col">
              <div id="cart-right-content">
                <h1> Total: $100</h1>
                <Button onClick={handleCheckout}>Checkout</Button>
              </div>
            </Col>
          </Row>
        </Container>
      </div>
    </>
  );
};

export default Cart;
