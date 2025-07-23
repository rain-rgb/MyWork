const path = require('path')
const CompressionPlugin = require('compression-webpack-plugin')

function resolve(dir) {
  return path.join(__dirname, dir)
}

const webpack = require('webpack')
module.exports={
  configureWebpack:{
    plugins:[
      new webpack.ProvidePlugin({
        $: 'jquery',
        jQuery: 'jquery'
      })
    ]
  }
}

// vue.config.js
module.exports = {
  configureWebpack: {
    externals: {
      AMap: 'window.AMap',
      AMapUI: 'window.AMapUI' // 高德地图配置
    }
  },
  /*
    Vue-cli3:
    Crashed when using Webpack `import()` #2463
    https://github.com/vuejs/vue-cli/issues/2463
   */
  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  // 多入口配置
  // pages: {
  //   index: {
  //     entry: 'src/main.js',
  //     template: 'public/index.html',
  //     filename: 'index.html',
  //   }
  // },
  // 打包app时放开该配置
  // publicPath:'./',
  configureWebpack: config => {
    // 生产环境取消 console.log
    if (process.env.NODE_ENV === 'production') {
      config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true
    }
  },
  chainWebpack: (config) => {
    config.resolve.alias
      .set('@$', resolve('src'))
      .set('@api', resolve('src/api'))
      .set('@assets', resolve('src/assets'))
      .set('@comp', resolve('src/components'))
      .set('@views', resolve('src/views'))
      .set('@layout', resolve('src/layout'))
      .set('@static', resolve('src/static'))
      .set('@mobile', resolve('src/modules/mobile'))

    // 生产环境，开启js\css压缩
    if (process.env.NODE_ENV === 'production') {
      config.plugin('compressionPlugin').use(new CompressionPlugin({
        test: /\.(js|css|less)$/, // 匹配文件名
        threshold: 10240, // 对超过10k的数据压缩
        deleteOriginalAssets: false // 不删除源文件
      }))
    }

    // 配置 webpack 识别 markdown 为普通的文件
    config.module
      .rule('markdown')
      .test(/\.md$/)
      .use()
      .loader('file-loader')
      .end()

    // 编译vxe-table包里的es6代码，解决IE11兼容问题
    config.module
      .rule('vxe')
      .test(/\.js$/)
      .include
      .add(resolve('node_modules/vxe-table'))
      .add(resolve('node_modules/vxe-table-plugin-antd'))
      .end()
      .use()
      .loader('babel-loader')
      .end()
  },

  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          /* less 变量覆盖，用于自定义 ant design 主题 */
          'primary-color': '#0C264D',
          'link-color': '#0C264D',
          'border-radius-base': '4px',
          fixedHeader: true
          // 'primary-color': '#9932cc',
          // 'link-color': '#9932cc',
          // 'border-radius-base': '4px',
        },
        javascriptEnabled: true
      }
    }
  },

  devServer: {
    port: 3000,
    proxy: {
      // '/api': {
      //    target: 'https://mock.ihx.me/mock/5baf3052f7da7e07e04a5116/antd-pro', // mock API接口系统
      //    ws: false,
      //    changeOrigin: true,
      //    pathRewrite: {
      //      '/jeecg-boot': '' // 默认所有请求都加了jeecg-boot前缀，需要去掉
      //    }
      //  },
      '/jeecg-boot': {
        target: 'http://localhost:8080/', // 请求本地 需要jeecg-boot后台项目 192.168.1.2:8080
        ws: false,
        changeOrigin: true
      },
      // '/js': {
      //   target: 'http://js.traiot.cn/', // mock API接口系统(视频查看获取token取消跨域问题(通过后台接口获取))
      //   ws: false,
      //   changeOrigin: true,
      //   pathRewrite: {
      //     '^/js': ''
      //   }
      // }
    }
  },

  lintOnSave: undefined
}
