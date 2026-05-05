import { useEffect, useState } from "react";
import { commentsApi } from "../services/api";

export default function CommentSection({ noteId }) {
  const [comments, setComments] = useState([]);
  const [content, setContent] = useState("");

  useEffect(() => {
    if (!noteId) {
      return;
    }
    commentsApi.listByNote(noteId).then((response) => setComments(response.data));
  }, [noteId]);

  const submit = async () => {
    if (!content.trim()) {
      return;
    }
    const response = await commentsApi.create({ noteId, content });
    setComments((prev) => [...prev, response.data]);
    setContent("");
  };

  return (
    <aside className="flex h-full w-80 flex-col border-l border-gray-200 bg-gray-50/70">
      <div className="border-b border-gray-200 bg-white p-4">
        <h4 className="flex items-center gap-2 text-sm font-semibold">
          <span className="material-symbols-outlined text-base">chat_bubble</span>
          Comments
        </h4>
      </div>
      <div className="flex-1 space-y-3 overflow-y-auto p-4">
        {comments.map((comment) => (
          <article key={comment.id} className="rounded-xl border border-gray-100 bg-white p-3">
            <div className="mb-1 flex items-center justify-between">
              <span className="text-xs font-bold text-gray-800">{comment.author?.username || "User"}</span>
              <span className="text-[10px] text-gray-400">{new Date(comment.createdAt).toLocaleString()}</span>
            </div>
            <p className="text-xs text-gray-600">{comment.content}</p>
          </article>
        ))}
      </div>
      <div className="border-t border-gray-200 bg-white p-3">
        <div className="flex gap-2">
          <input
            value={content}
            onChange={(event) => setContent(event.target.value)}
            className="flex-1 rounded-lg border border-gray-200 px-3 py-2 text-xs"
            placeholder="Write a comment..."
          />
          <button onClick={submit} className="rounded-lg bg-indigo-600 px-3 py-2 text-xs font-semibold text-white">
            Send
          </button>
        </div>
      </div>
    </aside>
  );
}