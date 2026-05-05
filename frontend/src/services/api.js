import axios from "axios";
import { getToken } from "../utils/storage";

const api = axios.create({
  baseURL: "/api"
});

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authApi = {
  login: (payload) => api.post("/auth/login", payload),
  register: (payload) => api.post("/auth/register", payload)
};

export const notesApi = {
  list: (params = {}) => api.get("/notes", { params }),
  create: (payload) => api.post("/notes", payload),
  update: (id, payload) => api.put(`/notes/${id}`, payload),
  remove: (id) => api.delete(`/notes/${id}`),
  getOne: (id) => api.get(`/notes/${id}`),
  versions: (id) => api.get(`/notes/${id}/versions`),
  restore: (id, versionId) => api.post(`/notes/${id}/restore`, { versionId })
};

export const tagsApi = {
  list: () => api.get("/tags"),
  create: (payload) => api.post("/tags", payload)
};

export const searchApi = {
  search: (q) => api.get("/search", { params: { q } })
};

export const sharingApi = {
  share: (payload) => api.post("/share", payload),
  listShared: () => api.get("/shared")
};

export const commentsApi = {
  listByNote: (noteId) => api.get(`/comments/${noteId}`),
  create: (payload) => api.post("/comments", payload)
};

export const groupsApi = {
  list: () => api.get("/groups"),
  create: (payload) => api.post("/groups", payload)
};

export const notificationsApi = {
  list: () => api.get("/notifications")
};

export default api;