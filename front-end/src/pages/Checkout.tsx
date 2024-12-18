import { useEffect, useState } from "react";
import { Button, Col, Container, Row } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

interface CartItem {
  id: number;
  productId: number;
  name: string;
  category: string;
  description: string;
  price: number;
  imageBase64: string;
  quantity: number;
}

interface OrderItemRequest {
  productId: number;
  quantity: number;
  price: number;
}

const Checkout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const total: number = location.state?.total || 0;
  const address = localStorage.getItem("address");
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const token = localStorage.getItem("token");
  const email = localStorage.getItem("email");

  const getProductsInCart = async () => {
    if (!token) {
      console.log("Cannot access until user logs in");
      navigate("/login");
    } else {
      try {
        const response = await axiosInstance.get<CartItem[]>(
          `/api/cart-items/get/${email}`,
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
    }
  };

  useEffect(() => {
    getProductsInCart();
  }, []);

  const handlePlaceOrder = () => {
    try {
      const orderItemRequests: OrderItemRequest[] = cartItems.map((item) => ({
        productId: item.productId,
        quantity: item.quantity,
        price: item.price,
      }));

      const orderRequest = {
        email: email,
        totalAmount: total,
        createdAt: new Date().toISOString(),
        orderItemRequestDtos: orderItemRequests,
      };

      axiosInstance.post("/api/orders/create", orderRequest, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log("Order has been placed successfully");

      clearCart();

      navigate("/order");
    } catch (error) {
      console.log(error);
    }
  };

  const clearCart = async () => {
    try {
      await axiosInstance.delete(`/api/cart-items/clear/${email}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log("Cart has beeen cleared");
    } catch (error) {
      console.log(error);
    }
  };

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
