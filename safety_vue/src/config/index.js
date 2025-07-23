/** init domain config */
import Vue from 'vue'
//设置全局API_BASE_URL
// Vue.prototype.API_JS_URL = process.env.VUE_APP_API_JS_URL 视频查看获取token地址(通过后台接口获取)
Vue.prototype.API_BASE_URL = process.env.VUE_APP_API_BASE_URL
Vue.prototype.VUE_APP_API_SOCKET_URL = process.env.VUE_APP_API_SOCKET_URL
// window._CONFIG['JSURL'] = Vue.prototype.API_JS_URL 视频查看获取token地址(通过后台接口获取)
window._CONFIG['domianURL'] = Vue.prototype.API_BASE_URL
window._CONFIG['scoketURL'] = Vue.prototype.VUE_APP_API_SOCKET_URL
//单点登录地址
window._CONFIG['casPrefixUrl'] = process.env.VUE_APP_CAS_BASE_URL
window._CONFIG['onlinePreviewDomainURL'] =  process.env.VUE_APP_ONLINE_BASE_URL
window._CONFIG['staticDomainURL'] = Vue.prototype.API_BASE_URL + '/sys/common/static'
window._CONFIG['pdfDomainURL'] = Vue.prototype.API_BASE_URL+ '/sys/common/pdf/pdfPreviewIframe'