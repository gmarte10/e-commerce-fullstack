import axiosInstance from "../axiosInstance";

const useRegisterUser = () => {
    const registerUser = async (name: string, email: string, password: string, address: string, phone: string) => {
      try {

        const requestBody = {
            userDto: {
                name: name,
                email: email,
                password: password,
                address: address,
                phone: phone,
                role: "CUSTOMER"
            },
            password: password
        }
        const response = await axiosInstance.post(`/api/users/register`, requestBody);
        console.log(`User registerd: ${JSON.stringify(response.data)}`);
      } catch (error) {
        console.error(error);
        return "issue registering user";
      }
    };
    return registerUser;
  };
  export default useRegisterUser;