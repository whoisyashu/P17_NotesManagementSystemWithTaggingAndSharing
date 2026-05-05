import { useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthLayout from "../layouts/AuthLayout";
import { authApi } from "../services/api";
import { useAppStore } from "../store/useAppStore";

export default function SignupPage() {
  const navigate = useNavigate();
  const setAuth = useAppStore((state) => state.setAuth);
  const [form, setForm] = useState({ username: "", email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async (event) => {
    event.preventDefault();
    try {
      setLoading(true);
      setError("");
      const response = await authApi.register(form);
      setAuth({ user: response.data.user, token: response.data.token });
      navigate("/dashboard", { replace: true });
    } catch (requestError) {
      setError(requestError.response?.data?.message || "Signup failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout
      title="Create your account"
      subtitle="Start your journey to organized thinking today."
      sideTitle="Capture ideas before they flutter away."
      sideText="Organize notes, collaborate in real-time, and transform random thoughts into structured projects."
      alternatePath="/login"
      alternateText="Already have an account?"
      alternateCta="Log in"
    >
      <form onSubmit={submit} className="space-y-4">
        <div>
          <label className="mb-1 block text-sm font-semibold text-gray-700">Username</label>
          <input
            value={form.username}
            onChange={(event) => setForm((prev) => ({ ...prev, username: event.target.value }))}
            className="w-full rounded-lg border border-gray-200 px-3 py-2 text-sm"
            placeholder="john"
          />
        </div>
        <div>
          <label className="mb-1 block text-sm font-semibold text-gray-700">Email</label>
          <input
            type="email"
            value={form.email}
            onChange={(event) => setForm((prev) => ({ ...prev, email: event.target.value }))}
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
            placeholder="Minimum 8 characters"
          />
        </div>
        {error && <div className="rounded-lg border border-red-100 bg-red-50 px-3 py-2 text-sm text-red-600">{error}</div>}
        <button
          disabled={loading}
          className="flex w-full items-center justify-center gap-2 rounded-lg bg-indigo-600 py-2.5 text-sm font-semibold text-white disabled:opacity-60"
        >
          {loading ? "Creating account..." : "Sign up"}
          <span className="material-symbols-outlined text-base">arrow_forward</span>
        </button>
      </form>
    </AuthLayout>
  );
}