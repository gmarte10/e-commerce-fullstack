import { Outlet } from "react-router-dom";

/**
 * Layout component serves as a wrapper for routing in the application.
 * It renders the nested routes through the `Outlet` component from React Router.
 * 
 * @component
 * @example
 * return (
 *   <Layout />
 * )
 */

const Layout = () => {
  return (
    <>
      <Outlet />
    </>
  );
};

export default Layout;
