<template>

</template>

<script>
import axios from 'axios'
import Vue from "vue";
import { ACCESS_TOKEN, SAFETY_PARTIAL_BASIC } from '@/store/mutation-types'
import Cookies from 'js-cookie'
import {getActions} from '../../api/manage'

export default {
  name: "jiliangzhifu",
  mounted() {
    this.onchange()

  },
  data(){
    return{
      tokens:'',
      atavalue:{}
    }
  },
  methods:{
    onchange(){

      axios.interceptors.request.use(function (config) {
        const token=Vue.ls.get(ACCESS_TOKEN)
        console.log(token+"token")
        config.headers.common['X-Access-Token'] = token;
        return config
      }, function (error) {
        return Promise.reject(error)
      })

        axios.get(`${window._CONFIG['domianURL']}/jiliangzhifu/findToken`).then((res =>{
          console.log(res.data+'123')
          let atavalue={
            "appId": 1001,
            "authToken": res.data
          }

          getActions('http://47.100.92.98:8182/gateway/auth-server/auth/sign/token',
            atavalue).then(res=>{
           console.log(res)
            window.open("http://47.100.92.98:8182/meter-highway-web/#/login?token="+res.data+"")

          })
      }))
    }
  }
}
</script>

<style scoped>

</style>