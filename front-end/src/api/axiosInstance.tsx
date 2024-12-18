import axios from "axios";

/**
 * Axios instance configured with a base URL for making HTTP requests.
 * This instance is used throughout the application to interact with the backend API.
 *
 * The base URL is set to `http://localhost:8080`, which is assumed to be the server's endpoint during development.
 *
 * @module axiosInstance
 */

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
});

export default axiosInstance;
