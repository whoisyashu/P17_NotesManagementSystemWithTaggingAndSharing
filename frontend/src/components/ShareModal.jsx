import { useState } from "react";

export default function ShareModal({ open, onClose, onSubmit, groups = [] }) {
  const [targetType, setTargetType] = useState("user");
  const [targetValue, setTargetValue] = useState("");
  const [permission, setPermission] = useState("VIEW");

  if (!open) {
    return null;
  }

  const submit = () => {
    const payload = {
      permission,
      userId: targetType === "user" ? Number(targetValue) : null,
      groupId: targetType === "group" ? Number(targetValue) : null
    };
    onSubmit(payload);
    setTargetValue("");
  };

  return (
    <div className="fixed inset-0 z-[70] flex items-center justify-center bg-slate-900/40 p-4 backdrop-blur-sm">
      <div className="w-full max-w-lg rounded-2xl bg-white p-6 shadow-2xl">
        <div className="mb-5 flex items-center justify-between">
          <h3 className="text-xl font-bold text-gray-900">Share Note</h3>
          <button onClick={onClose} className="rounded-full p-1 text-gray-400 hover:bg-gray-100">
            <span className="material-symbols-outlined">close</span>
          </button>
        </div>

        <div className="space-y-4">
          <div>
            <label className="mb-1 block text-sm font-semibold text-gray-700">Share with</label>
            <select
              value={targetType}
              onChange={(e) => {
                setTargetType(e.target.value);
                setTargetValue("");
              }}
              className="w-full rounded-lg border border-gray-200 bg-white px-3 py-2 text-sm"
            >
              <option value="user">User ID</option>
              <option value="group">Group</option>
            </select>
          </div>

          {targetType === "group" ? (
            <div>
              <label className="mb-1 block text-sm font-semibold text-gray-700">Group</label>
              <select
                value={targetValue}
                onChange={(e) => setTargetValue(e.target.value)}
                className="w-full rounded-lg border border-gray-200 bg-white px-3 py-2 text-sm"
              >
                <option value="">Select a group</option>
                {groups.map((group) => (
                  <option key={group.id} value={group.id}>
                    {group.name}
                  </option>
                ))}
              </select>
            </div>
          ) : (
            <div>
              <label className="mb-1 block text-sm font-semibold text-gray-700">User ID</label>
              <input
                value={targetValue}
                onChange={(e) => setTargetValue(e.target.value)}
                className="w-full rounded-lg border border-gray-200 px-3 py-2 text-sm"
                placeholder="Enter user id"
              />
            </div>
          )}

          <div>
            <label className="mb-1 block text-sm font-semibold text-gray-700">Permission</label>
            <select
              value={permission}
              onChange={(e) => setPermission(e.target.value)}
              className="w-full rounded-lg border border-gray-200 bg-white px-3 py-2 text-sm"
            >
              <option value="VIEW">Can view</option>
              <option value="EDIT">Can edit</option>
            </select>
          </div>
        </div>

        <div className="mt-6 flex justify-end gap-2">
          <button onClick={onClose} className="rounded-lg border border-gray-200 px-4 py-2 text-sm font-semibold text-gray-700">
            Cancel
          </button>
          <button
            onClick={submit}
            disabled={!targetValue}
            className="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-50"
          >
            Invite
          </button>
        </div>
      </div>
    </div>
  );
}