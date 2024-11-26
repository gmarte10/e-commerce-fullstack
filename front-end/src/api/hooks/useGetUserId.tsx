import axiosInstance from "../axiosInstance";

const useGetUserId = () => {

    const getUserId = async (email: string | null) => {
        try {
          const token = localStorage.getItem("token");
          const response = await axiosInstance.get(`/api/users/id/${email}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            }
          });
          localStorage.setItem("user-id", response.data);
          return response.data;
        } catch (error) {
          console.log(error);
          return null;
        }
      };
      return getUserId;
}
 
export default useGetUserId;