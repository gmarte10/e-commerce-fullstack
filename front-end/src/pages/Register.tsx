import { useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";
import "../css/register.css";

/**
 * Register component allows users to create a new account by providing their personal details.
 * The component collects user information such as email, password, name, address, and phone number.
 * Upon successful registration, the user is redirected to the login page.
 * 
 * @component
 * @example
 * return (
 *   <Register />
 * )
 */

const Register = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [address, setAddress] = useState("");
  const [phone, setPhone] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    registerUser();
    navigate("/login");
  };

  const registerUser = async () => {
    try {
      const requestBody = {
        name: name,
        email: email,
        role: "CUSTOMER",
        address: address,
        phone: phone,
        password: password,
      };
      await axiosInstance.post(`/api/users/register`, requestBody);
      console.log(`User registerd successfully`);
    } catch (error) {
      console.log(error);
    }
  };

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };
  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAddress(e.target.value);
  };
  const handlePhoneChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPhone(e.target.value);
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  return (
    <>
      <div className="register-form">
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label>Email address</Form.Label>
            <Form.Control
              type="email"
              placeholder="Enter email"
              value={email}
              onChange={handleEmailChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              placeholder="Password"
              value={password}
              onChange={handlePasswordChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicName">
            <Form.Label>Name</Form.Label>
            <Form.Control
              type="text"
              placeholder="name"
              value={name}
              onChange={handleNameChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicAddress">
            <Form.Label>Address</Form.Label>
            <Form.Control
              type="text"
              placeholder="address"
              value={address}
              onChange={handleAddressChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicPhone">
            <Form.Label>Phone</Form.Label>
            <Form.Control
              type="tel"
              placeholder="phone"
              value={phone}
              onChange={handlePhoneChange}
            />
          </Form.Group>
          <Button id="register-submit-btn" variant="primary" type="submit">
            Register
          </Button>
        </Form>
      </div>
    </>
  );
};

export default Register;
