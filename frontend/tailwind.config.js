/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#3525cd",
        primaryContainer: "#4f46e5",
        background: "#fcf8ff",
        outline: "#777587",
        outlineVariant: "#c7c4d8"
      },
      fontFamily: {
        h1: ["Poppins", "sans-serif"],
        h2: ["Poppins", "sans-serif"],
        body: ["Inter", "sans-serif"],
        accent: ["Plus Jakarta Sans", "sans-serif"]
      }
    }
  },
  plugins: []
};