import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import CommentSection from "../components/CommentSection";
import Editor from "../components/Editor";
import ShareModal from "../components/ShareModal";
import TagChip from "../components/TagChip";
import { groupsApi, notesApi, sharingApi, tagsApi } from "../services/api";
import { useAppStore } from "../store/useAppStore";
import { debounce } from "../utils/debounce";
import { useNoteCollaboration } from "../hooks/useNoteCollaboration";

export default function NoteEditorPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const noteId = id === "new" ? null : Number(id);

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [allTags, setAllTags] = useState([]);
  const [selectedTagIds, setSelectedTagIds] = useState([]);
  const [versions, setVersions] = useState([]);
  const [groups, setGroups] = useState([]);
  const [shareOpen, setShareOpen] = useState(false);

  const upsertNote = useAppStore((state) => state.upsertNote);

  useEffect(() => {
    tagsApi.list().then((response) => setAllTags(response.data));
    groupsApi.list().then((response) => setGroups(response.data));
  }, []);

  useEffect(() => {
    if (!noteId) {
      return;
    }
    notesApi.getOne(noteId).then((response) => {
      const note = response.data;
      setTitle(note.title);
      setContent(note.content);
      setSelectedTagIds((note.tags || []).map((tag) => tag.id));
    });
    notesApi.versions(noteId).then((response) => setVersions(response.data));
  }, [noteId]);

  const autosave = useMemo(
    () =>
      debounce(async (nextTitle, nextContent) => {
        if (!noteId) {
          return;
        }
        const response = await notesApi.update(noteId, {
          title: nextTitle,
          content: nextContent,
          tagIds: selectedTagIds
        });
        upsertNote(response.data);
      }, 700),
    [noteId, selectedTagIds, upsertNote]
  );

  const onRemoteUpdate = useCallback(
    (updatedNote) => {
      setTitle(updatedNote.title);
      setContent(updatedNote.content);
      setSelectedTagIds((updatedNote.tags || []).map((tag) => tag.id));
      upsertNote(updatedNote);
    },
    [upsertNote]
  );

  useNoteCollaboration(noteId, onRemoteUpdate);

  const onContentChange = (nextContent) => {
    setContent(nextContent);
    autosave(title, nextContent);
  };

  const onTitleChange = (nextTitle) => {
    setTitle(nextTitle);
    autosave(nextTitle, content);
  };

  const saveNow = async () => {
    if (!title.trim()) {
      return;
    }
    if (!noteId) {
      const response = await notesApi.create({ title, content, tagIds: selectedTagIds });
      upsertNote(response.data);
      navigate(`/notes/${response.data.id}`, { replace: true });
      return;
    }
    const response = await notesApi.update(noteId, { title, content, tagIds: selectedTagIds });
    upsertNote(response.data);
    const latestVersions = await notesApi.versions(noteId);
    setVersions(latestVersions.data);
  };

  const toggleTag = (tagId) => {
    setSelectedTagIds((prev) => (prev.includes(tagId) ? prev.filter((idValue) => idValue !== tagId) : [...prev, tagId]));
  };

  const restoreVersion = async (versionId) => {
    if (!noteId) {
      return;
    }
    const response = await notesApi.restore(noteId, versionId);
    onRemoteUpdate(response.data);
    const latestVersions = await notesApi.versions(noteId);
    setVersions(latestVersions.data);
  };

  const shareNote = async (payload) => {
    if (!noteId) {
      return;
    }
    await sharingApi.share({ ...payload, noteId });
    setShareOpen(false);
  };

  return (
    <section className="flex min-h-[calc(100vh-96px)] gap-6">
      <div className="flex-1 rounded-xl border border-gray-200 bg-white p-6">
        <div className="mb-4 flex items-center justify-between">
          <div className="flex flex-wrap gap-2">
            {allTags.map((tag, index) => (
              <button key={tag.id} onClick={() => toggleTag(tag.id)} className="rounded-full">
                <TagChip label={tag.name} tone={["indigo", "green", "blue", "amber"][index % 4]} />
              </button>
            ))}
          </div>
          <div className="flex gap-2">
            {noteId && (
              <button
                onClick={() => setShareOpen(true)}
                className="flex items-center gap-1 rounded-lg border border-indigo-600 px-3 py-2 text-xs font-bold text-indigo-700"
              >
                <span className="material-symbols-outlined text-sm">share</span>
                Share
              </button>
            )}
            <button onClick={saveNow} className="rounded-lg bg-indigo-600 px-4 py-2 text-xs font-bold text-white">
              Save
            </button>
          </div>
        </div>

        <input
          value={title}
          onChange={(event) => onTitleChange(event.target.value)}
          className="mb-4 w-full border-none p-0 font-h1 text-4xl font-semibold text-gray-900 outline-none"
          placeholder="Note Title"
        />
        <Editor value={content} onChange={onContentChange} />

        {!!versions.length && (
          <section className="mt-6 rounded-xl border border-gray-200 bg-gray-50/70 p-4">
            <h3 className="mb-3 text-sm font-bold text-gray-700">Version History</h3>
            <div className="space-y-2">
              {versions.slice(0, 5).map((version) => (
                <div key={version.id} className="flex items-center justify-between rounded-lg bg-white px-3 py-2">
                  <div>
                    <p className="text-sm font-semibold text-gray-800">Version #{version.versionNumber}</p>
                    <p className="text-xs text-gray-500">{new Date(version.createdAt).toLocaleString()}</p>
                  </div>
                  <button
                    onClick={() => restoreVersion(version.id)}
                    className="rounded-lg border border-gray-200 px-3 py-1.5 text-xs font-semibold text-indigo-700"
                  >
                    Restore
                  </button>
                </div>
              ))}
            </div>
          </section>
        )}
      </div>

      {noteId && <CommentSection noteId={noteId} />}
      <ShareModal open={shareOpen} onClose={() => setShareOpen(false)} onSubmit={shareNote} groups={groups} />
    </section>
  );
}