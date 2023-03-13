/** @type {import('tailwindcss').Config} */
const defaultTheme = require("tailwindcss/defaultTheme");

module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        "twitter-bg": "rgb(21, 32, 43)",
        "twitter-border": "rgb(66, 83, 100)",
        "twitter-button-border": "rgb(83, 100, 113)",
        "twitter-link": "rgb(107, 201, 251)",
        "twitter-hover-bg": "rgba(107, 201, 251, 0.1)",
      },
      fontFamily: {
        sans: ["Inter var", ...defaultTheme.fontFamily.sans],
      },
      fontSize: {
        twitter: "15px",
        twitterButton: "13px",
      },
    },
  },
  plugins: [],
};
