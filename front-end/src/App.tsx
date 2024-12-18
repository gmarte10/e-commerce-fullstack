import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import Layout from "./pages/Layout";
import Login from "./pages/Login";
import Home from "./pages/Home";
import Register from "./pages/Register";
import Product from "./pages/Product";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import Order from "./pages/Order";
import AdminHome from "./pages/admin/AdminHome";
import AddProduct from "./pages/admin/AddProduct";

/**
 * The main `App` component is responsible for setting up routing within the application.
 * It uses React Router to define routes for different views like login, home, register, product details, cart, checkout, orders, and admin functionalities.
 * 
 * @component
 * @example
 * return (
 *   <App />
 * )
 */

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route path="login" element={<Login />} />
            <Route path="home" element={<Home />} />
            <Route path="register" element={<Register />} />
            <Route path ="product" element={<Product />} />
            <Route path ="cart" element={<Cart/>} />
            <Route path ="checkout" element={<Checkout/>} />
            <Route path="order" element={<Order/>} />
            <Route path="admin-home" element={<AdminHome/>} />
            <Route path="add-product" element={<AddProduct/>} />
          </Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
