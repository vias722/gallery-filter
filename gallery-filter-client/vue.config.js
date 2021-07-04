module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
      port: 8081,
      proxy: 'http://localhost:8080'
  },
  outputDir: '../gallery-filter-server/src/main/resources/static'
};
