import { useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import AdminNavBar from "../../components/AdminNavBar";
import axiosInstance from "../../api/axiosInstance";

const AddProduct = () => {
  const [image, setImage] = useState<File | null>(null);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("");
  const [price, setPrice] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      console.log("Cannot access until user logs in");
      navigate("/login");
    }
  }, [navigate]);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    await addProduct();
    navigate("/admin-home");
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setImage(file);
    }
  };
  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  const handleDescriptionChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setDescription(e.target.value);
  };

  const handleCategoryChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCategory(e.target.value);
  };
  const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPrice(e.target.value);
  };

  const addProduct = async () => {
    try {
      const token = localStorage.getItem("token");
      console.log("Adding product...");

      const formData = new FormData();
      formData.append("name", name);
      formData.append("category", category);
      formData.append("description", description);
      formData.append("price", price);
      if (image) {
        formData.append("imageFile", image);
      }

      const request = {
        name: name,
        price: price,
        category: category,
        description: description,
        imageFile: image,
      };

      await axiosInstance.post(`/api/products/create`, request, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      console.log("Successfully added new product");
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <AdminNavBar />
      <div className="add-product-form">
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3" controlId="formBasicName">
            <Form.Label>Name</Form.Label>
            <Form.Control
              type="text"
              placeholder="name"
              value={name}
              onChange={handleNameChange}
            />
            <Form.Label>Description</Form.Label>
            <Form.Control
              type="text"
              placeholder="description"
              value={description}
              onChange={handleDescriptionChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicCategory">
            <Form.Label>Category</Form.Label>
            <Form.Control
              type="text"
              placeholder="category"
              value={category}
              onChange={handleCategoryChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicPrice">
            <Form.Label>Price</Form.Label>
            <Form.Control
              type="text"
              placeholder="price"
              value={price}
              onChange={handlePriceChange}
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId="formBasicImage">
            <Form.Label>Image</Form.Label>
            <Form.Control
              type="file"
              placeholder="product image"
              onChange={handleImageChange}
            />
          </Form.Group>
          <Button id="upload-submit-btn" variant="primary" type="submit">
            Upload
          </Button>
        </Form>
      </div>
    </>
  );
};

export default AddProduct;
