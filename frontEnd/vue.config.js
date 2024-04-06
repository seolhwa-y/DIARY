module.exports = {
  devServer: {
    proxy: {
      "/": {
        target: "http://LOCALHOST",
        pathRewrite: { "^/": "" },
        changeOrigin: true,
        secure: false,
      },
    },
  },
};

// 요청하는 URL을 부를 때 localhost가 server와 통신할 때 에러가 나기 때문에
// 이를 방지하고자 해당 부분을 설정해 줌
