import {
  Col,
  Container,
  ListGroup,
  Navbar,
  Row,
} from "react-bootstrap";

import NavBar from "../components/NavBar";
import { useEffect, useState } from "react";
import axiosInstance from "../api/axiosInstance";

interface Order {
  id: number;
  totalAmount: number;
  createdAt: Date;
  orderItemIds: number[];
}

interface OrderItem {
  id: number;
  orderId: number;
  name: string;
  category: string;
  description: string;
  price: number;
  imageBase64: string;
  quantity: number;
}

interface OrderWithItems {
  order: Order;
  items: OrderItem[];
}


const Order = () => {
  const [ordersWithItems, setOrdersWithItems] = useState<OrderWithItems[]>([]);
  const [loading, setLoading] = useState(true);

  const getOrders = async () => {
    try {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");

      const orderResponse = await axiosInstance.get<Order[]>(
        `/api/orders/get/${email}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const orders = orderResponse.data;

      const ordersWithItemsPromises = orders.map(async (order) => {
        const itemsResponse = await axiosInstance.get<OrderItem[]>(
          `/api/order-items/get/${order.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        return { order, items: itemsResponse.data};
      })
      const ordersWithItems = await Promise.all(ordersWithItemsPromises);
      console.log(ordersWithItems);
      setOrdersWithItems(ordersWithItems);
      
    } catch (error) {
      console.log(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    getOrders();
  }, []);

  if (loading) {
    return <div>Loading ...</div>;
  }

  return (
    <>
      <NavBar />
      <Container>
        <ListGroup>
          {ordersWithItems.map((orderWithItems, index) => (
            <ListGroup.Item key={index}>
              <Navbar expand="lg" className="bg-body-tertiary">
                <Container>
                  <Navbar.Text>Order Placed: {new Date(orderWithItems.order.createdAt).toLocaleString()}</Navbar.Text>
                  <Navbar.Text>Total: ${orderWithItems.order.totalAmount.toFixed(2)}</Navbar.Text>
                </Container>
              </Navbar>
              <Row>
                {orderWithItems.items.map((item) => (
                  <Row key={item.id} className="order-row">
                    <Col className="order-col" md={2}>
                      <img
                        className="order-image"
                        src={item.imageBase64}
                        alt="order image"
                      />
                    </Col>
                    <Col md={10} className="order-col">
                      <div className="order-p">
                        <h5>{item.name}</h5>
                        <p>Quantity: {item.quantity}</p>
                        <p>Price: ${item.price.toFixed(2)}</p>
                      </div>
                    </Col>
                  </Row>
                ))}
              </Row>
            </ListGroup.Item>
          ))}
        </ListGroup>
      </Container>
    </>
  );
};

export default Order;
