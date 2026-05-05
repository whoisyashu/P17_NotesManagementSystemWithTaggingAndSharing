import { Link } from "react-router-dom";

export default function AuthLayout({ title, subtitle, sideTitle, sideText, children, alternatePath, alternateText, alternateCta }) {
  return (
    <main className="flex min-h-screen flex-col bg-gradient-to-br from-gray-50 via-white to-indigo-100/30 md:flex-row">
      <section className="hidden flex-1 flex-col justify-between bg-indigo-600 p-10 text-white md:flex">
        <div>
          <div className="mb-6 flex items-center gap-2">
            <span className="material-symbols-outlined text-3xl">description</span>
            <span className="font-accent text-3xl font-black">BudNotes</span>
          </div>
          <h1 className="font-h1 text-4xl font-semibold leading-tight">{sideTitle}</h1>
          <p className="mt-4 max-w-md text-lg text-indigo-100">{sideText}</p>
        </div>
        <p className="text-sm uppercase tracking-widest text-indigo-200">Collaborate. Organize. Ship faster.</p>
      </section>

      <section className="flex flex-1 items-center justify-center p-6">
        <div className="w-full max-w-md rounded-2xl border border-gray-100 bg-white p-8 shadow-lg">
          <h2 className="font-h2 text-3xl font-semibold text-gray-900">{title}</h2>
          <p className="mt-2 text-sm text-gray-500">{subtitle}</p>
          <div className="mt-6">{children}</div>
          <p className="mt-6 text-center text-sm text-gray-600">
            {alternateText} <Link to={alternatePath} className="font-semibold text-indigo-600 hover:underline">{alternateCta}</Link>
          </p>
        </div>
      </section>
    </main>
  );
}