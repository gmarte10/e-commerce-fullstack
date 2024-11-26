import axiosInstance from "../axiosInstance";

const useAddCartItem = () => {
    const addCartItem = async (userId: number, productId: number, quantity: number) => {
      try {
        const token = localStorage.getItem("token");

        const requestBody = {
            cartItemDto: {
                userId: userId,
                productId: productId,
                quantity: quantity
            }
        }
        const response = await axiosInstance.post(`/api/cart-items/add`, requestBody, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        console.log(`CartItem Added: ${JSON.stringify(response.data)}`);
      } catch (error) {
        console.error(error);
        return "issue adding cart item";
      }
    };
    return addCartItem;
  };
  export default useAddCartItem;