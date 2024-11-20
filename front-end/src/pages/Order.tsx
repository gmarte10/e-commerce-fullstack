import {
  Button,
  Card,
  Col,
  Container,
  ListGroup,
  Navbar,
  Row,
} from "react-bootstrap";

import sonym5 from "../assets/sonym5.jpg";
import iphone from "../assets/iphone15.jpg";
import celtics from "../assets/celticshoodie.jpg";
import chicken from "../assets/ccsandwich.jpg";
import NavBar from "../components/NavBar";

interface Order {
  id: number;
  date: string;
  total: number;
  address: string;
  orderItems: OrderItem[];
}

interface OrderItem {
  id: number;
  name: string;
  price: number;
  category: string;
  description: string;
  imageBase64: string;
  quantity: number;
}

const orderItem: OrderItem[] = [
  {
    id: 1,
    name: "Iphone 15",
    price: 900,
    category: "Tech",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: iphone,
    quantity: 2
  },
  {
    id: 2,
    name: "Sony m5 headphones",
    price: 499.99,
    category: "Tech",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: sonym5,
    quantity: 1
  },
  {
    id: 3,
    name: "Celtics Hoodie",
    price: 120,
    category: "Clothing",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: celtics,
    quantity: 3
  },
  {
    id: 4,
    name: "Crispy Chicken Sandwich",
    price: 10,
    category: "Food",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: chicken,
    quantity: 1
  },
];
const orders: Order[] = [
  {
    id: 1,
    date: "2021-10-10",
    total: 1000,
    address: "1234 Main St",
    orderItems: [orderItem[0], orderItem[1]],
  },
  {
    id: 2,
    date: "2021-10-11",
    total: 130,
    address: "1234 Main St",
    orderItems: [orderItem[2], orderItem[3]],
  },
];

const Order = () => {
  return (
    <>
      <NavBar />
      <Container>
        <ListGroup>
          {orders.map((order) => (
            <ListGroup.Item key={order.id}>
              <Navbar expand="lg" className="bg-body-tertiary">
                <Container>
                  <Navbar.Text>Order Placed: {order.date}</Navbar.Text>
                  <Navbar.Text>Total: ${order.total}</Navbar.Text>
                </Container>
              </Navbar>
              <Row>
                {order.orderItems.map((item) => (
                  <Row className="order-row">
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
