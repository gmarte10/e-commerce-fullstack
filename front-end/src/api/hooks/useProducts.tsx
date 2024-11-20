import axiosInstance from "../axiosInstance";

const useGetProducts = () => {
    const getProducts = async () => {
        try {
          const token = localStorage.getItem("token");
          const response = await axiosInstance.get(`/api/products/get-all`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          // console.log(`Products: ${JSON.stringify(response.data)}`);
          return response.data;
        } catch (error) {
          console.log(error);
          return null;
        }
      };
      return getProducts;
}
export default useGetProducts;