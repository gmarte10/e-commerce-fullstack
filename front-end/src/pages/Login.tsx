import { useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import loginToken from "../api/hooks/useGetToken";
import useGetRole from "../api/hooks/useGetRole";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const getRole = useGetRole();
  const getToken = loginToken();
  const navigate = useNavigate();

  const loginUser = async () => {
    try {
      const login = await getToken(email, password);
      const role = await getRole(email);
      console.log(`Role: ${role}`);
      console.log(`Login: ${login}`);
      if (role === "ADMIN") {
        navigate("adminHome");
      } else {
        navigate("/home");
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    loginUser();
    navigate("/home");
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
      <div className = "login-container">
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
