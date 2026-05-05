import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AuthLayout from "../layouts/AuthLayout";
import { authApi } from "../services/api";
import { useAppStore } from "../store/useAppStore";

export default function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const setAuth = useAppStore((state) => state.setAuth);
  const [form, setForm] = useState({ usernameOrEmail: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async (event) => {
    event.preventDefault();
    try {
      setLoading(true);
      setError("");
      const response = await authApi.login(form);
      setAuth({ user: response.data.user, token: response.data.token });
      navigate(location.state?.from || "/dashboard", { replace: true });
    } catch (requestError) {
      setError(requestError.response?.data?.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout
      title="Welcome back"
      subtitle="Please enter your details to continue."
      sideTitle="Organize your thoughts with precision."
      sideText="The ultimate workspace for thinkers, creators, and teams to build their best work together."
      alternatePath="/signup"
      alternateText="Don't have an account?"
      alternateCta="Sign up"
    >
      <form onSubmit={submit} className="space-y-4">
        <div>
          <label className="mb-1 block text-sm font-semibold text-gray-700">Email or Username</label>
          <input
            value={form.usernameOrEmail}
            onChange={(event) => setForm((prev) => ({ ...prev, usernameOrEmail: event.target.value }))}
            className="w-full rounded-lg border border-gray-200 px-3 py-2 text-sm"
            placeholder="name@company.com"
          />
        </div>
        <div>
          <label className="mb-1 block text-sm font-semibold text-gray-700">Password</label>
          <input
            type="password"
            value={form.password}
            onChange={(event) => setForm((prev) => ({ ...prev, password: event.target.value }))}
            className="w-full rounded-lg border border-gray-200 px-3 py-2 text-sm"
            placeholder="••••••••"
          />
        </div>
        {error && <div className="rounded-lg border border-red-100 bg-red-50 px-3 py-2 text-sm text-red-600">{error}</div>}
        <button
          disabled={loading}
          className="flex w-full items-center justify-center gap-2 rounded-lg bg-indigo-600 py-2.5 text-sm font-semibold text-white disabled:opacity-60"
        >
          {loading ? "Signing in..." : "Login"}
          <span className="material-symbols-outlined text-base">arrow_forward</span>
        </button>
      </form>
    </AuthLayout>
  );
}