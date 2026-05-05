import { useEffect, useMemo, useState } from "react";
import { useNavigate, useOutletContext } from "react-router-dom";
import NoteCard from "../components/NoteCard";
import TagChip from "../components/TagChip";
import { notesApi, notificationsApi, searchApi, tagsApi } from "../services/api";
import { useAppStore } from "../store/useAppStore";
import { useDebouncedValue } from "../hooks/useDebouncedValue";

export default function DashboardPage() {
  const navigate = useNavigate();
  const { search } = useOutletContext();
  const debouncedSearch = useDebouncedValue(search, 400);

  const notes = useAppStore((state) => state.notes);
  const setNotes = useAppStore((state) => state.setNotes);
  const removeNote = useAppStore((state) => state.removeNote);
  const tags = useAppStore((state) => state.tags);
  const setTags = useAppStore((state) => state.setTags);
  const setNotifications = useAppStore((state) => state.setNotifications);

  const [selectedTag, setSelectedTag] = useState(null);

  useEffect(() => {
    notesApi.list({ size: 100 }).then((response) => {
      const payload = response.data;
      setNotes(payload.content || payload);
    });
    tagsApi.list().then((response) => setTags(response.data));
    notificationsApi.list().then((response) => setNotifications(response.data));
  }, [setNotes, setTags, setNotifications]);

  useEffect(() => {
    if (!debouncedSearch?.trim()) {
      notesApi.list({ size: 100 }).then((response) => {
        const payload = response.data;
        setNotes(payload.content || payload);
      });
      return;
    }
    searchApi.search(debouncedSearch).then((response) => setNotes(response.data));
  }, [debouncedSearch, setNotes]);

  const filteredNotes = useMemo(() => {
    if (!selectedTag) {
      return notes;
    }
    return notes.filter((note) => (note.tags || []).some((tag) => tag.id === selectedTag));
  }, [notes, selectedTag]);

  const deleteNote = async (id) => {
    await notesApi.remove(id);
    removeNote(id);
  };

  return (
    <section>
      <div className="mb-8 flex items-end justify-between">
        <div>
          <h2 className="font-h1 text-4xl font-semibold text-gray-900">Dashboard</h2>
          <p className="mt-2 text-gray-500">All your notes, shared context, and tagged knowledge in one place.</p>
        </div>
        <button
          onClick={() => navigate("/notes/new")}
          className="flex items-center gap-2 rounded-xl bg-indigo-600 px-5 py-3 text-sm font-bold text-white shadow-md"
        >
          <span className="material-symbols-outlined text-base">add</span>
          Create Note
        </button>
      </div>

      <div className="mb-6 flex flex-wrap items-center gap-2">
        <button
          onClick={() => setSelectedTag(null)}
          className={`rounded-full px-3 py-1 text-xs font-semibold ${
            selectedTag === null ? "bg-indigo-600 text-white" : "bg-white text-gray-700 border border-gray-200"
          }`}
        >
          All
        </button>
        {tags.map((tag, index) => (
          <button key={tag.id} onClick={() => setSelectedTag(tag.id)} className="rounded-full">
            <TagChip label={tag.name} tone={["indigo", "green", "blue", "amber"][index % 4]} />
          </button>
        ))}
      </div>

      <div className="grid grid-cols-1 gap-5 md:grid-cols-2 xl:grid-cols-3">
        {filteredNotes.map((note) => (
          <NoteCard key={note.id} note={note} onOpen={(id) => navigate(`/notes/${id}`)} onDelete={deleteNote} />
        ))}
      </div>
    </section>
  );
}