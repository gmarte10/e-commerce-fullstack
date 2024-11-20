import axiosInstance from "../axiosInstance";

const useGetRole = () => {

    const getRole = async (email: string) => {
        try {
          const token = localStorage.getItem("token");
          const response = await axiosInstance.get(`/api/users/role/${email}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            }
          });
          return response.data;
        } catch (error) {
          console.log(error);
          return null;
        }
      };
      return getRole;
}
 
export default useGetRole;