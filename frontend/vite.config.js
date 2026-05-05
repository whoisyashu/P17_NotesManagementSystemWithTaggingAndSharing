import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

const backendTarget = process.env.VITE_BACKEND_URL || "http://localhost:8081";

export default defineConfig({
  define: {
    global: "globalThis"
  },
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      "/api": {
        target: backendTarget,
        changeOrigin: true
      },
      "/ws": {
        target: backendTarget,
        ws: true,
        changeOrigin: true
      }
    }
  }
});