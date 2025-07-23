<template>
<div></div>
</template>

<script>
import axios from 'axios'
import Vue from "vue";
import { ACCESS_TOKEN, SAFETY_PARTIAL_BASIC } from '@/store/mutation-types'


export default {
  name: "pingan",
  mounted() {
    this.onchange()
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

      axios.get(`${window._CONFIG['domianURL']}/pingan/find`).then((res =>{
        console.log(res,'平安守护')
         window.open(res.data)
      }))
      }
    }
  }

</script>

<style scoped>

</style>