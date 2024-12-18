import { useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";
import '../css/login.css';

interface UserInfo {
  name: string;
  phone: string;
  address: string;
  role: string;
}

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const loginUser = async () => {
    try {
      const loginResponse = await axiosInstance.post(`/api/users/login`, {
        email: email,
        password: password,
      });
      localStorage.setItem("token", loginResponse.data);
      localStorage.setItem("email", email);

      const infoResponse = await axiosInstance.get<UserInfo>(
        `/api/users/info/${email}`,
        {
          headers: {
            Authorization: `Bearer ${loginResponse.data}`,
          },
        }
      );
      localStorage.setItem("name", infoResponse.data.name);
      localStorage.setItem("phone", infoResponse.data.phone);
      localStorage.setItem("address", infoResponse.data.address);
      localStorage.setItem("role", infoResponse.data.role);
      console.log("Logging in...");
    } catch (error) {
      console.error(error);
    }
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    await loginUser();
    const role = localStorage.getItem("role");
    if (role === "ADMIN") {
      console.log("Navigating to admin home...");
      navigate("/admin-home");
    } else {
      navigate("/home");
    }
  };

  const handleRegister = () => {
    navigate("/register");
  };

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  return (
    <>
      <div className="login-container">
        <div className="login-form">
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
            <Button id="login-submit-btn" variant="primary" type="submit">
              Login
            </Button>
          </Form>
        </div>
        <div className="login-register">
          <Button
            id="login-register-btn"
            variant="primary"
            type="submit"
            onClick={handleRegister}
          >
            Register
          </Button>
        </div>
      </div>
    </>
  );
};

export default Login;
