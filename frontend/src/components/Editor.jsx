import ReactQuill from "react-quill";

const modules = {
  toolbar: [
    [{ header: [1, 2, 3, false] }],
    ["bold", "italic", "underline"],
    [{ list: "ordered" }, { list: "bullet" }],
    ["link", "image"],
    ["clean"]
  ]
};

export default function Editor({ value, onChange }) {
  return (
    <div className="rounded-xl border border-gray-200 bg-white p-4">
      <ReactQuill theme="snow" value={value} onChange={onChange} modules={modules} className="min-h-[260px]" />
    </div>
  );
}