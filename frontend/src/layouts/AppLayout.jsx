import { Outlet } from "react-router-dom";
import { useState } from "react";
import Sidebar from "./Sidebar";
import Navbar from "./Navbar";

export default function AppLayout() {
  const [search, setSearch] = useState("");

  return (
    <div className="min-h-screen bg-gray-50">
      <Sidebar />
      <Navbar search={search} onSearch={setSearch} />
      <main className="ml-64 mt-16 p-8">
        <Outlet context={{ search }} />
      </main>
    </div>
  );
}