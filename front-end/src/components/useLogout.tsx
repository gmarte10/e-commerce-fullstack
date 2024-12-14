const useLogout = () => {
    const logout = async () => {
        localStorage.removeItem("token");
        localStorage.removeItem("email");
        localStorage.removeItem("name");
        localStorage.removeItem("phone");
        localStorage.removeItem("address");
        localStorage.removeItem("role");
        console.log("User logged out");
    }
    return logout;
}
 
export default useLogout;