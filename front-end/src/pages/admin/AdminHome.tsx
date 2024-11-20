import sonym5 from "../../assets/sonym5.jpg";
import iphone from "../../assets/iphone15.jpg";
import celtics from "../../assets/celticshoodie.jpg";
import chicken from "../../assets/ccsandwich.jpg";
import { useNavigate } from "react-router-dom";
import {
  Button,
  Card,
  Col,
  Container,
  Form,
  InputGroup,
  Row,
} from "react-bootstrap";
import { useState } from "react";
import AdminNavBar from "../../components/AdminNavBar";

interface Prod {
  id: number;
  name: string;
  price: number;
  category: string;
  description: string;
  imageBase64: string;
}

const products: Prod[] = [
  {
    id: 1,
    name: "Iphone 15",
    price: 900,
    category: "Tech",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: iphone,
  },
  {
    id: 2,
    name: "Sony m5 headphones",
    price: 499.99,
    category: "Tech",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: sonym5,
  },
  {
    id: 3,
    name: "Celtics Hoodie",
    price: 120,
    category: "Clothing",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: celtics,
  },
  {
    id: 4,
    name: "Crispy Chicken Sandwich",
    price: 10,
    category: "Food",
    description:
      "Lorem ipsum odor amet, consectetuer adipiscing elit. Lorem aliquet quisque bibendum ullamcorper per. Primis maecenas porttitor finibus elementum conubia suspendisse lobortis amet nec.",
    imageBase64: chicken,
  },
];

const AdminHome = () => {
  const navigate = useNavigate();
  const [search, setSearch] = useState("");

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
  };

  const handleSearch = () => {
    // search for products and update the products state
  };

  return (
    <>
      <AdminNavBar />
      <Container>
        <div className="search-bar">
          <InputGroup className="mb-3">
            <Form.Control
              placeholder="search"
              aria-label="search-bar"
              aria-describedby="search-bar"
              value={search}
              onChange={handleSearchChange}
            />
            <Button variant="outline-secondary" id="search-bar-btn">
              Search
            </Button>
          </InputGroup>
        </div>
        <Row className="g-4">
          {products.map((product) => (
            <Col key={product.id}>
              <Card
                style={{ width: "18rem" }}
              >
                <Card.Img
                  className="product-image"
                  variant="top"
                  src={product.imageBase64}
                />
                <Card.Body className="home-card-body">
                  <Card.Title>{product.name}</Card.Title>
                  <Card.Text>${product.price}</Card.Text>
                  <Button variant="primary">Remove</Button>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </>
  );
};

export default AdminHome;
