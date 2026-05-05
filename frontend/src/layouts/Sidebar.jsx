import { Link, useLocation } from "react-router-dom";

const links = [
  { icon: "description", label: "All Notes", to: "/dashboard" },
  { icon: "share", label: "Shared Notes", to: "/shared" },
  { icon: "group", label: "Groups", to: "/groups" },
  { icon: "label", label: "Tags", to: "/dashboard#tags" }
];

export default function Sidebar() {
  const location = useLocation();

  return (
    <aside className="fixed left-0 top-0 z-50 flex h-screen w-64 flex-col space-y-2 border-r border-gray-200 bg-white px-4 py-6">
      <div className="mb-8 px-2">
        <h1 className="font-accent text-2xl font-black text-indigo-600">BudNotes</h1>
        <p className="mt-1 text-xs font-semibold uppercase tracking-wider text-gray-500">Workspace</p>
      </div>
      <Link
        to="/notes/new"
        className="mb-4 flex w-full items-center justify-center gap-2 rounded-xl bg-indigo-600 py-3 text-sm font-bold text-white shadow-lg shadow-indigo-100"
      >
        <span className="material-symbols-outlined text-base">add</span>
        Create Note
      </Link>
      <nav className="flex-1 space-y-1">
        {links.map((link) => {
          const active = location.pathname === link.to;
          return (
            <Link
              key={link.to}
              to={link.to}
              className={`flex items-center gap-3 border-l-4 px-4 py-2 text-sm font-semibold transition-all ${
                active
                  ? "border-indigo-600 bg-indigo-50 text-indigo-700"
                  : "border-transparent text-gray-600 hover:bg-gray-50 hover:text-indigo-600"
              }`}
            >
              <span className="material-symbols-outlined text-base">{link.icon}</span>
              {link.label}
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}