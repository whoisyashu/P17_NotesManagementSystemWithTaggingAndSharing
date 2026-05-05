import { create } from "zustand";
import { clearToken, clearUser, getToken, getUser, setToken, setUser } from "../utils/storage";

const initialUser = getUser();
const initialToken = getToken();

export const useAppStore = create((set) => ({
  user: initialUser,
  token: initialToken,
  notes: [],
  selectedNote: null,
  tags: [],
  notifications: [],
  loading: false,

  setLoading: (loading) => set({ loading }),

  setAuth: ({ user, token }) => {
    if (token) {
      setToken(token);
    }
    if (user) {
      setUser(user);
    }
    set({ user, token });
  },

  logout: () => {
    clearToken();
    clearUser();
    set({ user: null, token: null, notes: [], selectedNote: null, tags: [], notifications: [] });
  },

  setNotes: (notes) => set({ notes }),
  setSelectedNote: (selectedNote) => set({ selectedNote }),
  setTags: (tags) => set({ tags }),
  setNotifications: (notifications) => set({ notifications }),

  upsertNote: (updatedNote) =>
    set((state) => {
      const exists = state.notes.some((note) => note.id === updatedNote.id);
      const notes = exists
        ? state.notes.map((note) => (note.id === updatedNote.id ? updatedNote : note))
        : [updatedNote, ...state.notes];
      return {
        notes,
        selectedNote:
          state.selectedNote && state.selectedNote.id === updatedNote.id ? updatedNote : state.selectedNote
      };
    }),

  removeNote: (id) =>
    set((state) => ({
      notes: state.notes.filter((note) => note.id !== id),
      selectedNote: state.selectedNote?.id === id ? null : state.selectedNote
    }))
}));