/**
 * 根据当前选择的组织机构去更新当前组织机构下有哪些设备的权限
 */
import Vue from 'vue'
import { changearryOne } from '@/api/api'
import {SYS_DEPART_ORGCODE} from "@/store/mutation-types";

export const pushdepartidShebei = {
  data() {
    return {
      sys_depart_orgcode:'',
    }

  },
  created() {
    this.sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
    console.log(this.sys_depart_orgcode,"刷新设备的组织机构")
  },
  methods:{
    changearryOneshebei(){
      console.log(this.sys_depart_orgcode,"刷新设备的组织机构")
      changearryOne({//根据当前用户选择的组织机构改变其所拥有的权限的设备信息
        sys_depart_orgcode:this.sys_depart_orgcode
      }).then(res=>{
        console.log("获取当前用户下的设备信息")
      })
    },
    routeReload() {//选择全局组织机构之后刷新当前的右侧页面  解决 没这个方法之前点击之后缓存数据不更新
      this.reloadFlag = false
      let ToggleMultipage = 'ToggleMultipage'
      this.$store.dispatch(ToggleMultipage, false)
      this.$nextTick(() => {
        this.$store.dispatch(ToggleMultipage, true)
        this.reloadFlag = true
      })
      console.log("刷新页面")
    },
  }
}
