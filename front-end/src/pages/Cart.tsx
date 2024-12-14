import { Button, Col, Container, Row } from "react-bootstrap";
import NavBar from "../components/NavBar";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axiosInstance from "../api/axiosInstance";

interface CartItem {
  id: number;
  name: string;
  category: string;
  description: string;
  price: number;
  imageBase64: string;
  quantity: number;
}

const Cart = () => {
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [total, setTotal] = useState(0);

  const getProductsInCart = async () => {
    try {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      console.log(`Email: ${email}`);
      console.log(`Token: ${token}`);
      const response = await axiosInstance.get<CartItem[]>(
        `/api/cart-items/get/${email}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(response.data[0]);
      setCartItems(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const handleRemoveCartItem = async (cartItemId: number) => {
    try {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      console.log(`Email: ${email}`);
      console.log(`Token: ${token}`);
      const response = await axiosInstance.delete(
        `/api/cart-items/remove/${cartItemId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(`CartItem Removed: ${JSON.stringify(response.data)}`);
      getProductsInCart();
    } catch (error) {
      console.error(error);
      return "issue removing cart item";
    }
  };

  const getTotal = () => {
    let total = 0;
    cartItems.forEach((cartItem) => {
      total += cartItem.price * cartItem.quantity;
    });
    return total;
  }

  useEffect(() => {
    getProductsInCart();
  }, []);

  useEffect(() => {
    setTotal(getTotal());
  }, [cartItems]);

  const handleCheckout = () => {
    navigate(`/checkout`, { state: { total } });
  };
  return (
    <>
      <NavBar />
      <div className="cart">
        <Container>
          <Row>
            <Col md={6}>
              {cartItems.map((cartItem, index) => (
                <Row className="cart-product" key={index}>
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
                        <p>{cartItem.id}</p>
                        <p>${cartItem.price}</p>
                        <p>Quantity: {cartItem.quantity}</p>
                      </div>
                    </div>
                  </Col>
                  <Col className="cart-col-btn">
                    <div className="cart-div-rm-btn">
                      <Button onClick = {() => {handleRemoveCartItem(cartItem.id)}}>Remove</Button>
                    </div>
                  </Col>
                </Row>
              ))}
            </Col>
            <Col md={6} id="cart-right-col">
              <div id="cart-right-content">
                <h1> Total: ${total}</h1>
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
