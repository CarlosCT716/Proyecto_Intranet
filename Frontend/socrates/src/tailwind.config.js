/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  safelist: [
    'bg-blue-600',
    'bg-purple-600',
    'bg-green-600',
    'bg-red-600',
    "bg-[#0B4D6C]",
    'bg-yellow-600',
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}