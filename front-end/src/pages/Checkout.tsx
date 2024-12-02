import { useEffect, useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

interface CartItem {
  productId: number;
  name: string;
  price: number;
  category: string;
  description: string;
  imageBase64: string;
  quantity: number;
  cartItemId: number;
}



const Checkout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const total: number = location.state?.total || 0;
  const [address, setAddress] = useState("");
  const [cartItems, setCartItems] = useState<CartItem[]>([]);

  const getProductsInCart = async () => {
    try {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      const response = await axiosInstance.get<CartItem[]>(
        `/api/cart-items/${email}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setCartItems(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    getProductsInCart();
  }, []);


  const handlePlaceOrder = () => {
    try {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      axiosInstance.post(
        "/api/orders/add",
        {
          email: email,
          cartItemIds: cartItems.map((cartItem) => cartItem.cartItemId),
          total: total,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
    } catch (error) {
      console.log(error);
    }
    navigate("/order");
  };

  const getAddress = async () => {
    try {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      console.log(`Email: ${email}`);
      console.log(`Token: ${token}`);
      const response = await axiosInstance.get<string>(
        `/api/users/address/${email}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setAddress(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    getAddress();
  }, []);

  return (
    <>
      <Container className="checkout-container">
        <Row>
          <Col>
            <Row className="checkout-row">
              <h2>Shipping Address</h2>
              <h4>{address}</h4>
            </Row>
            <Row className="checkout-row">
              <h2>Payment</h2>
              <span>
                <div className="pay-btn-div">
                  <Button className="pay-btn">Paypal</Button>
                  <Button className="pay-btn">Credit Card</Button>
                </div>
              </span>
            </Row>
          </Col>
          <Col className="checkout-col">
            <div className="checkout-col-content">
              <h2>Order Total</h2>
              <p>${total}</p>
              <Button onClick={handlePlaceOrder}>Place Order</Button>
            </div>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Checkout;
