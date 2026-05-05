import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAppStore } from "../store/useAppStore";

export default function PrivateRoute() {
  const token = useAppStore((state) => state.token);
  const location = useLocation();

  if (!token) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }

  return <Outlet />;
}