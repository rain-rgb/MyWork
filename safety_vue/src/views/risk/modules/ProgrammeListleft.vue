<template>
<a-card :loading="cardLoading" :bordered="false" style="height: 100%;">
<a-spin :spinning="loading">
<a-input-search @search="handleSearch" style="width:100%;margin-top: 10px" placeholder="输入机构名称查询..." enterButton />
 <a-tree
        showLine
        checkStrictly
        :expandedKeys.sync="expandedKeys"
        :selectedKeys="selectedKeys"
        :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
        :treeData="treeDataSource"
        @select="handleTreeSelect"
      />
    </a-spin>
  </a-card>
</template>
<script>
import { queryDepartTreeList, searchByKeywords } from '@/api/api'
export default{
    name: 'ProgrammeListleft',
    props: ['value'],
    data() {
      return {
        cardLoading: true,
        loading: false,
        treeDataSource: [],
        selectedKeys: [],
        expandedKeys: []
      }
    },
    created() {
      this.queryTreeData()
    },
    methods: {

      queryTreeData(keyword) {
        this.commonRequestThen(queryDepartTreeList({
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

      handleTreeSelect(selectedKeys, event) {
        if (selectedKeys.length > 0 && this.selectedKeys[0] !== selectedKeys[0]) {
          this.selectedKeys = [selectedKeys[0]]
          let orgCode = event.node.dataRef.orgCode
          this.emitInput(orgCode)
        }
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