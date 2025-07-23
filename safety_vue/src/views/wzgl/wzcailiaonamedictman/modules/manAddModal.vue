<template>
  <j-modal
    centered
    :title="title"
    :width="1200"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    okText="导入材料"
  >
    <a-form >
      <a-card class="material" title="基础信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{textAlign:'center'}">
      <a-row >
        <a-col :span="8">
          <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol"  required>
            <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"  v-model="modelData.sysOrgCode"/>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="使用电子锁"  :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag  v-model="modelData.iselocks"  dictCode="yn" placeholder="请选择是否使用电子锁"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="称重分类"  :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list"  v-model="modelData.lmcailiaolx"
                               :trigger-change="true" dictCode="lmcailiaolx" placeholder="请选择材料分类"/>
          </a-form-item>
        </a-col>
      </a-row>
      </a-card>
        <a-card class="material" title="材料选择" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{textAlign:'center'}">
      <a-row >
        <a-col :span="8">
          <a-form-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag placeholder="请选择材料类型" v-model="queryParam1.nodetype" :dictCode=codeType></j-dict-select-tag>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label=材料名称 :labelCol="labelCol" :wrapperCol="wrapperCol" >
            <a-input placeholder="请输入材料名称" v-model="queryParam1.cailiaoname"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8">
            <span >
              <a-button type="primary" @click="search" icon="search">查询</a-button>
            </span>

        </a-col>
      </a-row>
      <a-table
        ref="table"
        size="middle"
        bordered
        :rowKey="item=>JSON.stringify(item)"
        class="j-table-force-nowrap"
        :scroll="{ x: true }"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :loading="loading"
        @change="tableChange"
      >
                <span slot="stirDatetime" slot-scope="stirDatetime, record">
                    <a-tag color="green">{{ record.stirDatetime }}</a-tag>
                </span>
      </a-table>
        </a-card>
    </a-form>
  </j-modal>
</template>

<script>
import { getAction, postAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import Vue from "vue";
import {DEPART_ID } from '@/store/mutation-types'

export default {
  name: 'manAddModal',
  components: { JeecgListMixin,JselectDqDepart },
  props: ['dictType' ],
  data() {
    return {
      dataSource: [],
      codeType:'nodetypeCP',
      selectedRowKeys: [],
      selectionRows: [],
      gblx: 0,
      modelData: {},
      loading: false,
      visible: false,
      title: '材料导入',
      queryParam1:{},
      labelCol: {
        xs: { span: 8 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 8 },
        sm: { span: 12 },
      },
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      columns: [
        {
          title: '#',
          dataIndex: '',
          key:'rowIndex',
          width:60,
          align:"center",
          customRender:function (t,r,index) {
            return parseInt(index)+1;
          }
        },
        {
          title:'材料类型',
          align:"center",
          dataIndex: 'nodetype_dictText'
        },

        {
          title:'材料名称',
          align:"center",
          dataIndex: 'cailiaoname'
        },

        {
          title:'规格型号',
          align:"center",
          dataIndex: 'guigexinghao'
        },
        {
          title:'描述',
          align:"center",
          dataIndex: 'miaoshu'
        }

      ],
      url: {
        bhTimeList: '/wzcailiaonamedictall/wzcailiaonamedictAll/list',
        updateBatch: '/wzcailiaonamedictman/wzcailiaonamedictMan/addBatch',
        updateBatch2: '/wzcailiaonamedict/wzcailiaonamedict/addBatch',
      },
    }
  },
  created() {},
  methods: {
    getListXq() {
      this.loading = true
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        gblx:this.gblx,
        column: 'id',
        order: 'desc',
      }
      if(this.queryParam1.cailiaoname !== undefined){
        params.cailiaoname = this.queryParam1.cailiaoname + '*'
      }
      if(this.queryParam1.nodetype !== undefined){
        params.nodetype = this.queryParam1.nodetype
      }

      getAction(this.url.bhTimeList, params)
        .then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            if (res.result.total) {
              this.ipagination.total = res.result.total
            } else {
              this.ipagination.total = 0
            }
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    routeReload() {//刷新页面
      this.reloadFlag = false
      let ToggleMultipage = 'ToggleMultipage'
      this.$store.dispatch(ToggleMultipage, false)
      this.$nextTick(() => {
        this.$store.dispatch(ToggleMultipage, true)
        this.reloadFlag = true
      })
      console.log("刷新页面")
    },
    getList(a) {
     if(a == 0){
       this.codeType = 'nodetypeCP'
       this.gblx = 1
     }else{
       this.codeType = 'nodetypeCL'
       this.gblx = 0
     }
      this.ipagination.current = 1
      this.ipagination.pageSize = 10

      this.selectedRowKeys = []
      this.selectionRows = []
      this.getListXq()
      this.visible = true
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectedRowKeys
      // this.selectionRows = selectionRows
      // this.selectionRows =this.selectionRows.concat(selectionRows)

    },
    tableChange(pagination) {
      this.ipagination.current = pagination.current
      this.ipagination.pageSize = pagination.pageSize
      this.getListXq()
    },
    search() {
      this.ipagination.current = 1
      this.ipagination.pageSize = 10
      this.getListXq()
    },
    handleCancel() {
      this.selectionRows = []
      this.visible = false
    },
    handleOk() {
      if (this.selectionRows.length > 0) {
        let params = this.modelData
        if(params.sysOrgCode == undefined){
          this.$message.warning("请选择所属部门！！！")
        }else{

          if(this.dictType == 0){
            let wzcailiaonamedictManList = []
            this.selectionRows.forEach((item)=>{
              wzcailiaonamedictManList.push( JSON.parse(item))
            })
            params.wzcailiaonamedictManList =  wzcailiaonamedictManList;
            postAction(this.url.updateBatch, params).then((res) => {
              if (res.success) {
                this.$message.success('保存成功！')
                this.visible = false
                this.selectionRows = []
                this.routeReload()
              } else {
                this.$message.error('保存失败！')
              }
            })
          }else{
            let wzcailiaonamedictList = []
            this.selectionRows.forEach((item)=>{
              wzcailiaonamedictList.push( JSON.parse(item))
            })
            params.wzcailiaonamedictList =  wzcailiaonamedictList;
            postAction(this.url.updateBatch2, params).then((res) => {
              if (res.success) {
                this.$message.success('保存成功！')
                this.visible = false
                this.selectionRows = []
                this.routeReload()
              } else {
                this.$message.error('保存失败！')
              }
            })
          }

        }


      } else {
        this.$message.warning('请选择材料后再进行数据保存！')
      }
    },
  },
}
</script>
<style scoped>
</style>