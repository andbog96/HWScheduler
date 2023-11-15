module.exports = {
  devServer: {
    port: 8000,
    proxy: {
      '/': {
        target: 'http://localhost:5000',
        ws: true,
        changeOrigin: true
      }
    }
  }
}