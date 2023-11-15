module.exports = {
  devServer: {
    port: 8000,
    proxy: {
      '/api/v1': {
        target: 'http://localhost:9091',
        ws: true,
        changeOrigin: true
      }
    }
  }
}