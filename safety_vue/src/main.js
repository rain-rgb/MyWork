/** init domain config */
import './config'

import Vue from 'vue'
import App from './App.vue'
import Storage from 'vue-ls'
import router from './router'
import store from './store/'
import { VueAxios } from "@/utils/request"
import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'
import VueQr from "vue-qr";
import print from "print-js";

new Vue({
  components: {VueQr}
});

require('@jeecg/antd-online-mini')
require('@jeecg/antd-online-mini/dist/OnlineForm.css')

import Antd, { version } from 'ant-design-vue'
console.log('ant-design-vue version:', version)

import * as echarts from 'echarts'
Vue.prototype.$echarts = echarts //echarts 不能使用use

import http1 from '../src/api/requestt'
Vue.prototype.$http1 = http1

import Viser from 'viser-vue'
import 'ant-design-vue/dist/antd.less';  // or 'ant-design-vue/dist/antd.less'

import '@/permission' // permission control
import '@/utils/filter' // base filter
import Print from 'vue-print-nb-jeecg'

/*import '@babel/polyfill'*/
import $ from 'jquery'
Vue.prototype.$ = $
// Vue.use($)
import preview from 'vue-photo-preview'
import 'vue-photo-preview/dist/skin.css'
import SSO from '@/cas/sso.js'
import {
  ACCESS_TOKEN,
  DEFAULT_COLOR,
  DEFAULT_THEME,
  DEFAULT_LAYOUT_MODE,
  DEFAULT_COLOR_WEAK,
  SIDEBAR_TYPE,
  DEFAULT_FIXED_HEADER,
  DEFAULT_FIXED_HEADER_HIDDEN,
  DEFAULT_FIXED_SIDEMENU,
  DEFAULT_CONTENT_WIDTH_TYPE,
  DEFAULT_MULTI_PAGE
} from "@/store/mutation-types"
import config from '@/defaultSettings'

import JDictSelectTag from './components/dict/index.js'
import hasPermission from '@/utils/hasPermission'
import vueBus from '@/utils/vueBus';
import JeecgComponents from '@/components/jeecg/index'
import '@/assets/less/JAreaLinkage.less'
import VueAreaLinkage from 'vue-area-linkage'
import '@/components/jeecg/JVxeTable/install'
import '@/components/JVxeCells/install'
import VueAMap from 'vue-amap';
import AmapVue from '@amap/amap-vue'
AmapVue.config.key = 'b0db32ad411b2633f679d33d0bd94f05'
AmapVue.config.plugins = [
  'AMap.ToolBar',
  'AMap.MoveAnimation',
  // 在此配置你需要预加载的插件，如果不配置，在使用到的时候会自动异步加载
];
Vue.use(AmapVue)
Vue.use(VueAMap);
VueAMap.initAMapApiLoader({
  key: 'b0db32ad411b2633f679d33d0bd94f05',
  plugin: ['AMap.Autocomplete', 'AMap.PlaceSearch', 'AMap.Scale', 'AMap.OverView',
    'AMap.ToolBar', 'AMap.MapType', 'AMap.PolyEditor', 'AMap.CircleEditor', 'AMap.MarkerClusterer', 'AMap.MoveAnimation'
],
  // 默认高德 sdk 版本为 1.4.4
  uiVersion: '1.1'
});
Vue.config.productionTip = false
Vue.use(Storage, config.storageOptions)
Vue.use(Antd)
Vue.use(VueAxios, router)
Vue.use(Viser)
Vue.use(hasPermission)
Vue.use(JDictSelectTag)
Vue.use(Print)
Vue.use(preview)
Vue.use(vueBus);
Vue.use(JeecgComponents);
Vue.use(VueAreaLinkage);
Vue.use(Viewer)
Viewer.setDefaults({
  Options: {
    'inline': true, // 是否启用inline模式
    'button': true, // 是否显示右上角关闭按钮
    'navbar': false, // 是否显示缩略图底部导航栏
    'title': true, // 是否显示当前图片标题，默认显示alt属性内容和尺寸
    'toolbar': true, // 是否显示工具栏
    'tooltip': true, // 放大或缩小图片时，是否显示缩放百分比，默认true
    'fullscreen': true, // 播放时是否全屏，默认true
    'loading': true, // 加载图片时是否显示loading图标，默认true
    'loop': true, // 是否可以循环查看图片，默认true
    'movable': true, // 是否可以拖得图片，默认true
    'zoomable': true, // 是否可以缩放图片，默认true
    'rotatable': true, // 是否可以旋转图片，默认true
    'scalable': true, // 是否可以翻转图片，默认true
    'toggleOnDblclick': true, // 放大或缩小图片时，是否可以双击还原，默认true
    'transition': true, // 使用 CSS3 过度，默认true
    'keyboard': true, // 是否支持键盘，默认true
    'zoomRatio': 0.1, // 鼠标滚动时的缩放比例，默认0.1
    'minZoomRatio': 0.01, // 最小缩放比例，默认0.01
    'maxZoomRatio': 100, // 最大缩放比例，默认100
    'url': 'data-source' // 设置大图片的 url
  }
})

SSO.init(() => {
  main()
})
function main() {
  new Vue({
    router,
    store,
    beforeCreate() {
      //安装全局事件总线
      Vue.prototype.$bus = this
    },
    mounted () {
      store.commit('SET_SIDEBAR_TYPE', Vue.ls.get(SIDEBAR_TYPE, true))
      store.commit('TOGGLE_THEME', Vue.ls.get(DEFAULT_THEME, config.navTheme))
      store.commit('TOGGLE_LAYOUT_MODE', Vue.ls.get(DEFAULT_LAYOUT_MODE, config.layout))
      store.commit('TOGGLE_FIXED_HEADER', Vue.ls.get(DEFAULT_FIXED_HEADER, config.fixedHeader))
      store.commit('TOGGLE_FIXED_SIDERBAR', Vue.ls.get(DEFAULT_FIXED_SIDEMENU, config.fixSiderbar))
      store.commit('TOGGLE_CONTENT_WIDTH', Vue.ls.get(DEFAULT_CONTENT_WIDTH_TYPE, config.contentWidth))
      store.commit('TOGGLE_FIXED_HEADER_HIDDEN', Vue.ls.get(DEFAULT_FIXED_HEADER_HIDDEN, config.autoHideHeader))
      store.commit('TOGGLE_WEAK', Vue.ls.get(DEFAULT_COLOR_WEAK, config.colorWeak))
      store.commit('TOGGLE_COLOR', Vue.ls.get(DEFAULT_COLOR, config.primaryColor))
      store.commit('SET_TOKEN', Vue.ls.get(ACCESS_TOKEN))
      store.commit('SET_MULTI_PAGE',Vue.ls.get(DEFAULT_MULTI_PAGE,config.multipage))
    },
    render: h => h(App)
  }).$mount('#app')
}