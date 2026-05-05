import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { sharingApi } from "../services/api";

export default function SharedNotesPage() {
  const navigate = useNavigate();
  const [sharedNotes, setSharedNotes] = useState([]);

  useEffect(() => {
    sharingApi.listShared().then((response) => setSharedNotes(response.data));
  }, []);

  return (
    <section>
      <div className="mb-8">
        <h2 className="font-h1 text-4xl font-semibold text-gray-900">Shared Notes</h2>
        <p className="mt-2 text-gray-500">Notes shared with you by teammates and groups.</p>
      </div>
      <div className="space-y-3">
        {sharedNotes.map((item) => (
          <article key={item.id} className="flex items-center justify-between rounded-xl border border-gray-200 bg-white px-4 py-3">
            <div>
              <p className="text-sm font-semibold text-gray-900">Note #{item.noteId}</p>
              <p className="text-xs text-gray-500">
                Shared by {item.sharedBy?.username || "Unknown"} • Permission: {item.permission}
              </p>
            </div>
            <button
              onClick={() => navigate(`/notes/${item.noteId}`)}
              className="rounded-lg bg-indigo-50 px-3 py-1.5 text-xs font-bold text-indigo-700"
            >
              Open
            </button>
          </article>
        ))}
      </div>
    </section>
  );
}