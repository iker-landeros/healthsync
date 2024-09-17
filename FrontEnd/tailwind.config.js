/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
      'custom-purple': '#B687B8',
      'custom-green': '#AAAD00',
      'custom-orange': '#E67E3C',
      'custom-blue': '#7DAED3',
      },
    },
  },
  plugins: [],
}