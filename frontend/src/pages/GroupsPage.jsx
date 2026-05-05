import { useEffect, useState } from "react";
import { groupsApi } from "../services/api";

export default function GroupsPage() {
  const [groups, setGroups] = useState([]);
  const [name, setName] = useState("");

  const load = () => groupsApi.list().then((response) => setGroups(response.data));

  useEffect(() => {
    load();
  }, []);

  const create = async () => {
    if (!name.trim()) {
      return;
    }
    await groupsApi.create({ name, memberIds: [] });
    setName("");
    load();
  };

  return (
    <section>
      <div className="mb-8 flex items-end justify-between gap-4">
        <div>
          <h2 className="font-h1 text-4xl font-semibold text-gray-900">Groups</h2>
          <p className="mt-2 text-gray-500">Collaborate and share notes with teams and communities.</p>
        </div>
        <div className="flex gap-2">
          <input
            value={name}
            onChange={(event) => setName(event.target.value)}
            className="rounded-lg border border-gray-200 px-3 py-2 text-sm"
            placeholder="New group name"
          />
          <button onClick={create} className="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-semibold text-white">
            Create Group
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 gap-5 md:grid-cols-2 xl:grid-cols-3">
        {groups.map((group) => (
          <article key={group.id} className="rounded-xl border border-gray-200 bg-white p-5">
            <div className="mb-3 flex items-center justify-between">
              <div className="flex h-11 w-11 items-center justify-center rounded-lg bg-indigo-100 text-indigo-600">
                <span className="material-symbols-outlined">group</span>
              </div>
              <span className="rounded-full bg-indigo-50 px-2 py-1 text-[10px] font-bold uppercase text-indigo-700">
                {group.members?.length || 0} members
              </span>
            </div>
            <h3 className="text-lg font-semibold text-gray-900">{group.name}</h3>
            <p className="mt-2 text-xs text-gray-500">Owner: {group.owner?.username || "Unknown"}</p>
          </article>
        ))}
      </div>
    </section>
  );
}