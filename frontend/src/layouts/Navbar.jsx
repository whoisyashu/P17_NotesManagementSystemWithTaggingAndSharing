import { useState } from "react";
import { useNavigate } from "react-router-dom";
import SearchBar from "../components/SearchBar";
import NotificationDropdown from "../components/NotificationDropdown";
import { useAppStore } from "../store/useAppStore";

export default function Navbar({ search, onSearch }) {
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();
  const notifications = useAppStore((state) => state.notifications);
  const user = useAppStore((state) => state.user);
  const logout = useAppStore((state) => state.logout);

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  return (
    <header className="fixed left-64 right-0 top-0 z-40 flex h-16 items-center justify-between border-b border-gray-200 bg-white/90 px-8 backdrop-blur-md">
      <SearchBar value={search} onChange={onSearch} placeholder="Search notes, tags, or people..." />
      <div className="flex items-center gap-4">
        <div className="relative">
          <button onClick={() => setOpen((value) => !value)} className="relative rounded-full p-2 text-gray-500 hover:bg-gray-100">
            <span className="material-symbols-outlined">notifications</span>
            {notifications.some((item) => !item.read) && (
              <span className="absolute right-2 top-2 h-2 w-2 rounded-full border-2 border-white bg-indigo-600" />
            )}
          </button>
          {open && <NotificationDropdown notifications={notifications} />}
        </div>
        <div className="h-8 w-px bg-gray-200" />
        <button
          onClick={handleLogout}
          className="flex items-center gap-1 rounded-lg border border-gray-200 px-3 py-1.5 text-xs font-bold text-gray-700 hover:bg-gray-50"
          title="Logout"
        >
          <span className="material-symbols-outlined text-sm">logout</span>
          Logout
        </button>
        <div className="flex items-center gap-2">
          <div className="text-right">
            <p className="text-sm font-bold text-gray-800">{user?.username || "User"}</p>
            <p className="text-[10px] uppercase tracking-widest text-gray-500">Member</p>
          </div>
          <div className="flex h-8 w-8 items-center justify-center rounded-full bg-indigo-100 text-xs font-bold text-indigo-700">
            {(user?.username || "U").slice(0, 1).toUpperCase()}
          </div>
        </div>
      </div>
    </header>
  );
}