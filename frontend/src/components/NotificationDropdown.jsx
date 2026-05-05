export default function NotificationDropdown({ notifications }) {
  if (!notifications.length) {
    return (
      <div className="absolute right-0 z-50 mt-3 w-80 rounded-xl border border-gray-200 bg-white p-4 shadow-xl">
        <p className="text-sm text-gray-500">No notifications yet.</p>
      </div>
    );
  }

  return (
    <div className="absolute right-0 z-50 mt-3 w-80 overflow-hidden rounded-xl border border-gray-200 bg-white shadow-xl">
      <div className="border-b border-gray-100 bg-gray-50/70 p-3">
        <span className="text-sm font-bold">Notifications</span>
      </div>
      <div className="max-h-80 overflow-y-auto">
        {notifications.map((item) => (
          <div key={item.id} className="border-b border-gray-100 p-3">
            <p className="text-sm text-gray-800">{item.message}</p>
            <p className="mt-1 text-xs text-gray-500">{new Date(item.createdAt).toLocaleString()}</p>
          </div>
        ))}
      </div>
    </div>
  );
}