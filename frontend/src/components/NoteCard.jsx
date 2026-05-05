import TagChip from "./TagChip";

function toneByIndex(index) {
  const tones = ["indigo", "green", "blue", "amber", "rose", "slate"];
  return tones[index % tones.length];
}

export default function NoteCard({ note, onOpen, onDelete }) {
  return (
    <article className="group flex flex-col justify-between rounded-xl border border-gray-200 bg-white p-5 transition-all duration-300 hover:border-indigo-300/50 hover:shadow-xl">
      <div>
        <div className="mb-3 flex items-start justify-between gap-2">
          <h3 className="font-h2 text-lg font-semibold text-gray-900 transition-colors group-hover:text-indigo-700">{note.title}</h3>
          <button
            onClick={() => onDelete(note.id)}
            className="rounded p-1 text-gray-400 transition-colors hover:bg-gray-50 hover:text-red-500"
            title="Delete note"
          >
            <span className="material-symbols-outlined text-lg">delete</span>
          </button>
        </div>
        <p className="line-clamp-3 text-sm text-gray-600">{note.content?.replace(/<[^>]*>/g, "") || "No content yet"}</p>
      </div>

      <div className="mt-4">
        <div className="mb-3 flex flex-wrap gap-1.5">
          {(note.tags || []).slice(0, 3).map((tag, index) => (
            <TagChip key={tag.id || tag.name} label={tag.name} tone={toneByIndex(index)} small />
          ))}
        </div>
        <div className="flex items-center justify-between border-t border-gray-50 pt-3">
          <span className="text-xs text-gray-400">Updated {new Date(note.updatedAt).toLocaleString()}</span>
          <button
            onClick={() => onOpen(note.id)}
            className="rounded-lg bg-indigo-50 px-3 py-1.5 text-xs font-bold text-indigo-700 transition-colors hover:bg-indigo-100"
          >
            Open
          </button>
        </div>
      </div>
    </article>
  );
}