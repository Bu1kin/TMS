const colors = require('tailwindcss/colors')

module.exports = {
  purge: [],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      colors: {
        // Build your palette here
        transparent: 'transparent',
        current: 'currentColor',
        gray: colors.trueGray,
        red: colors.red,
        blue: colors.sky,
        yellow: colors.amber,
        customGray: '#1E293B',
      }
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
}