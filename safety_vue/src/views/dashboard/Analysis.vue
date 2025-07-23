<template>
  <div>
    <index-chart v-if="indexStyle==1"></index-chart>
    <index-bdc v-if="indexStyle==2"></index-bdc>
    <index-task v-if="indexStyle==3"></index-task>
<!--    <div style="width: 100%;text-align: right;margin-top: 20px">-->
<!--      请选择首页样式：-->
<!--      <a-radio-group v-model="indexStyle">-->
<!--        <a-radio :value="1">统计图表</a-radio>-->
<!--        <a-radio :value="2">统计图表2</a-radio>-->
<!--        <a-radio :value="3">任务表格</a-radio>-->
<!--      </a-radio-group>-->
<!--    </div>-->
  </div>
</template>

<script>
  import IndexChart from './IndexChart'
  import IndexTask from "./IndexTask"
  import IndexBdc from './IndexBdc'
  import Vue from "vue";
  import {ACCESS_TOKEN} from "@/store/mutation-types";
  import {mapActions} from "vuex";
  import store from '@/store/'
  import { USER_INFO } from '@/store/mutation-types'
  import { initialarryOne } from '@/api/api'


  export default {
    name: "Analysis",
    components: {
      IndexChart,
      IndexTask,
      IndexBdc
    },
    data() {
      return {
        indexStyle:1
      }
    },
    created() {
     //  debugger
     // var dadian = this.GetBearer('Bearer')
     //  if(dadian  != -1 ){
     //    console.log("dadian  != -1",dadian)
     //    this.currdatetime = new Date().getTime()
     //    Vue.ls.remove(ACCESS_TOKEN)
     //    this.getRouterData()
     //    this.Logins();
     //
     //  }
     //  console.log("dadian  === -1")
    },
    methods: {
      ...mapActions(['Login', 'Logout', 'PhoneLogin','Loginsso','Loginsso12']),

      Logins(){
        let that = this
        let loginParams = {}
        var username=this.GetQueryString("username")
        var token1 = this.GetQueryString("token")
        console.log(username,"当前用户")
        if(username!=null){
          loginParams.username =username;
          that.Loginsso(loginParams).then((res) => {
            console.log("res.result",res.result)
            this.$refs.loginSelect.show(res.result)
          }).catch((err) => {
            that.requestFailed(err)
          })
        }
        if(token1 != null){
          loginParams.username =token1;
          // loginParams.token =token1;
          that.Loginsso12(loginParams).then((res) => {
            console.log("res.result",res.result)
            this.$refs.loginSelect.show(res.result)
            initialarryOne().then(res => {//初始化当前登录用户的拥有的权限设备
              console.log('获取当前用户下的设备信息')
            })
          }).catch((err) => {
            that.requestFailed(err)
          })
        }
      },

      getRouterData() {
        this.$nextTick(() => {
          if (this.$route.params.username) {
            this.form.setFieldsValue({
              'username': this.$route.params.username
            })
          }
        })
      },

      GetQueryString(name){
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  decodeURIComponent(r[2]); return null;
      },

      GetBearer: function (bearer) {
       return window.location.href.indexOf(bearer)

      },

    }
  }
</script>