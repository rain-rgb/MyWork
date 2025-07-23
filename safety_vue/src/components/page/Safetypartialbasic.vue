<template>
  <a-card :loading="cardLoading" :bordered="false" style="height: 100%;">
    <a-spin :spinning="loading">
      <!-- <a-input-search @search="handleSearch" style="width:100%;margin-top: 10px" placeholder="输入机构名称查询..." enterButton /> -->

      <a-button type="primary" @click="resetPartial" icon="reload">取消选择</a-button>
      <a-tree
        showLine
        checkStrictly
        :expandedKeys.sync="expandedKeys"
        :selectedKeys="selectedKeys"
        :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
        :treeData="treeDataSource"
        :replace-fields="replaceFields"
        :load-data="onLoadData"
        @select="handleTreeSelect"
        @expand="onExpand"
      />
    </a-spin>
  </a-card>
</template>

<script>
import {
  queryPartialBasic,
  queryMyDepartprojectTreeList,
  searchByKeywords,
  searchByKeyprojectwords,
  queryDepartTreeprojectListSon
} from '@/api/api'
import Vue from 'vue'
import { SAFETY_PARTIAL_BASIC, SYS_DEPART_ORGCODE } from '@/store/mutation-types'
export default {
  name: 'Safetypartialbasic',
  props: ['value'],
  data() {
    return {
      replaceFields:{
        title: 'title',
        children: 'children',
        key: 'key'
      },
      parentId:"",
      cardLoading: true,
      loading: false,
      treeDataSource: [],
      selectedKeys: [],
      expandedKeys: [],
      sys_depart_project:'',
    }
  },
  created() {
    this.queryTreeData()
  },
  methods: {
    onLoadData(treeNode) {
      console.log(treeNode,"点击加载")
      return new Promise(resolve => {
        //console.log("异步进入")
        // if (treeNode.dataRef.children) {
        //   resolve();
        //   return;
        // }
        setTimeout(() => {
          //console.log("异步定时")
          let param={parentId:this.parentId}
          queryDepartTreeprojectListSon(param).then((res) => {
            if (res.success) {
              //console.log(res)
              let data=res.result;
              for (let i = 0; i < res.result.length; i++) {
                let temp = res.result[i]
                treeNode.dataRef.children.push(temp);
              }
              this.treeDataSource = [...this.treeDataSource];
              resolve();
            }
          })
        }, 500);
      });
    },
    onExpand(expandedKeys) {
      this.parentId="";
      console.log('onExpand', expandedKeys)
      // if not set autoExpandParent to false, if children expanded, parent can not collapse.
      // or, you can remove all expanded children keys.
      //this.iExpandedKeys = expandedKeys
      var length=expandedKeys.length;
      this.parentId=expandedKeys[length-1];
      console.log(this.parentId,"子节点数据")
      //this.autoExpandParent = false
    },
    queryTreeData(keyword) {
      let param={parentId:this.parentId}
      this.commonRequestThen(queryMyDepartprojectTreeList({
        parentId:this.parentId,
        departName: keyword ? keyword : undefined
      }))
    },

    handleSearch(value) {
      if (value) {
        this.commonRequestThen(searchByKeyprojectwords({ keyWord: value }))
      } else {
        this.queryTreeData()
      }
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
      this.sys_depart_project=Vue.ls.get('SAFETY_PARTIAL_BASIC');
     console.log("1111111111111"+this.sys_depart_project)
      //this.changearryOneshebei();
    },
    handleTreeSelect(selectedKeys, event) {
      //console.log(event)
      if (selectedKeys.length > 0 && this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]]
        let projectNo = event.node.dataRef.orgCode;
        //console.log(projectNo)
        Vue.ls.set('SAFETY_PARTIAL_BASIC', projectNo, 7 * 24 * 60 * 60 * 1000)////加入全局的分部分项的缓存
        console.log("组织机构"+ Vue.ls.get('SYS_DEPART_ORGCODE'));
        console.log("分部分项"+ Vue.ls.get('SAFETY_PARTIAL_BASIC'));
        //var lss=Vue.ls.get(FENBUFENX_PROJECT);
        //console.log(lss)
        this.emitInput(projectNo)
      }
      this.$emit('changeVisible1',false);
      this.routeReload();//刷新局部页面
    },

    emitInput(projectNo) {
      this.$emit('input', projectNo)
    },

    commonRequestThen(promise) {
      this.loading = true
      promise.then(res => {
        if (res.success) {
          console.log(res.result)
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

    resetPartial(){
      this.selectedKeys = []
      Vue.ls.set('SAFETY_PARTIAL_BASIC','');
      this.$emit('changeVisible1',false);
      this.routeReload();//刷新局部页面
    },

  }
}
</script>

<style scoped>

</style>