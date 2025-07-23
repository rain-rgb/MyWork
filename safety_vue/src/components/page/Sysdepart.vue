<template>
  <a-card :loading="cardLoading" :bordered="false" style="height: 100%;">
    <a-spin :spinning="loading">
      <!--      <a-input-search @search="handleSearch" style="width:100%;margin-top: 10px" placeholder="输入机构名称查询..." enterButton />-->
      <a-tree
        showLine
        checkStrictly
        :expandedKeys.sync="expandedKeys"
        :selectedKeys="selectedKeys"
        :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
        :treeData="treeDataSource"
        :visible="clonesvirr"
        @select="handleTreeSelect"
      />
    </a-spin>
  </a-card>
</template>
<script>
import Vue from 'vue'
import { queryMyDepartTreeList, searchByKeywords } from '@/api/api'
import { SAFETY_PARTIAL_BASIC, SYS_DEPART_ORGCODE, ONL_AUTH_FIELDS, USER_INFO } from '@/store/mutation-types'
import { changearryOne } from '@/api/api'
import {formatDate} from "@/utils/util";

export default {
  name: 'Sysdepart',
  props: ['value'],
  data() {
    return {
      clonesvirr: true,//传到父组件的参数
      cardLoading: true,
      loading: false,
      treeDataSource: [],
      selectedKeys: [],
      expandedKeys: [],
      sys_depart_orgcode:'',
    }
  },
  created() {
    this.queryTreeData()

  },
  methods: {

    queryTreeData(keyword) {
      //console.log(keyword)
      this.commonRequestThen(queryMyDepartTreeList({
        departName: keyword ? keyword : undefined
      }))
    },

    handleSearch(value) {
      if (value) {
        this.commonRequestThen(searchByKeywords({ keyWord: value }))
      } else {
        this.queryTreeData()
      }
    },
    changearryOneshebei(){
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
      this.sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
      console.log("获取当前用户下的设备信息"+this.sys_depart_orgcode)
      this.changearryOneshebei();
    },

    handleTreeSelect(selectedKeys, event) {
      console.log(selectedKeys)
      console.log(event)
      if (selectedKeys.length > 0 && this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]]
        let orgCode = event.node.dataRef.orgCode
        //console.log("组织机构"+orgCode)
        Vue.ls.set('SYS_DEPART_ORGCODE', orgCode, 7 * 24 * 60 * 60 * 1000)//把组织机构加入缓存
        //this.$store.commit("SYS_DEPART_ORGCODE",orgCode);
        console.log("组织机构"+ Vue.ls.get('SYS_DEPART_ORGCODE'));
        console.log("分部分项"+ Vue.ls.get('SAFETY_PARTIAL_BASIC'));
        this.emitInput(orgCode)
      }
      this.$emit('changeVisible',false);//传到父组件的参数 父组件通过changeVisible（value）方法修改你需要修改的值
      this.routeReload();//刷新局部页面
    },

    emitInput(orgCode) {
      this.$emit('input', orgCode)
    },

    commonRequestThen(promise) {
      this.loading = true
      promise.then(res => {
        if (res.success) {
          this.treeDataSource = res.result
          // update-begin- --- author:wangshuai ------ date:20200102 ---- for:去除默认选中第一条数据、默认展开所有第一级
          // 默认选中第一条数据、默认展开所有第一级
          // if (res.result.length > 0) {
          //   this.expandedKeys = []
          //   res.result.forEach((item, index) => {
          //     if (index === 0) {
          //       this.selectedKeys = [item.id]
          //       this.emitInput(item.orgCode)
          //     }
          //     this.expandedKeys.push(item.id)
          //   })
          // }
          // update-end- --- author:wangshuai ------ date:20200102 ---- for:去除默认选中第一条数据、默认展开所有第一级
        } else {
          this.$message.warn(res.message)
          console.error('组织机构查询失败:', res)
        }
      }).finally(() => {
        this.loading = false
        this.cardLoading = false
      })
    },

  }
}
</script>
<style scoped>

</style>