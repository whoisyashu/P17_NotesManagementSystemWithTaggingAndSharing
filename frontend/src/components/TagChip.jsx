export default function TagChip({ label, tone = "indigo", small = false }) {
  const palette = {
    indigo: "bg-indigo-50 text-indigo-700 border-indigo-100",
    green: "bg-emerald-50 text-emerald-700 border-emerald-100",
    blue: "bg-blue-50 text-blue-700 border-blue-100",
    amber: "bg-amber-50 text-amber-700 border-amber-100",
    rose: "bg-rose-50 text-rose-700 border-rose-100",
    slate: "bg-slate-50 text-slate-700 border-slate-100"
  };

  return (
    <span
      className={`inline-flex items-center rounded-full border px-2 py-1 font-semibold uppercase tracking-wider ${
        small ? "text-[10px]" : "text-xs"
      } ${palette[tone] || palette.indigo}`}
    >
      {label}
    </span>
  );
}