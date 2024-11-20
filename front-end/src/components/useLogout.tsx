const useLogout = () => {
    const logout = async () => {
        localStorage.removeItem("token");
        localStorage.removeItem("email");
        console.log("User logged out");
        return logout;
    }
}
 
export default useLogout;