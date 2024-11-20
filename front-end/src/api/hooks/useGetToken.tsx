import axiosInstance from "../axiosInstance";

const useGetToken = () => {
  const getToken = async (email: string, password: string) => {
    try {
      const response = await axiosInstance.post(`/api/users/login`, {
        email: email,
        password: password,
      });
      localStorage.setItem("token", response.data);
      localStorage.setItem("email", email);
      console.log(`Token: ${response.data}`);
      return "token retrieved and stored";
    } catch (error) {
      console.error(error);
      return "issue getting token";
    }
  };
  return getToken;
};
export default useGetToken;
