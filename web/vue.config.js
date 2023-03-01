/*
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2023-02-20 19:18:24
 * @FilePath: \web\vue.config.js
 */
const { defineConfig } = require('@vue/cli-service')
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin')
module.exports = defineConfig({
  transpileDependencies: true
})
module.exports = defineConfig({
  configureWebpack: {
  plugins: [new NodePolyfillPlugin()]
  }
})
