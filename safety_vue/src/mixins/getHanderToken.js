/**
 * 根据当前选择的组织机构去更新当前组织机构下有哪些设备的权限
 */
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export const handertoken = {
  data() {
    return {
      sys_depart_orgcode: '',
    }

  },
  created() {

  },
  methods: {
    getToken:function (){
      let queryString = this.getQueryString("token");
      if(queryString!=null || queryString!=undefined){
        console.log(queryString);
        Vue.ls.set(ACCESS_TOKEN, queryString, 7 * 24 * 60 * 60 * 1000)
      }
    },
    getQueryString(name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
      var r = window.location.search.substr(1).match(reg)
      if (r != null) return unescape(r[2])
      return null
    }
  }
}
