<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper" style="margin: 25px;margin-bottom: -25px">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeiNo" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出料时间范围">
              <j-date placeholder="开始出料时间" v-model="queryParam.productDatetime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束出料时间" v-model="queryParam.productDatetime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
          <!--            <a-form-item label="切换工程">-->
          <!--              <j-dict-select-tag placeholder="请选择工程" v-model="queryParam.poureLocation" dictCode="RCXM"></j-dict-select-tag>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>

            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator"> -->
    <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button> -->
    <!-- <a-button type="primary" icon="download" @click="handleExportXls('bhz_supervision_order')">导出</a-button> -->
    <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
      <a-button type="primary" icon="import">导入</a-button>
    </a-upload> -->
    <!-- 高级查询区域 -->
    <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
    <a-dropdown v-if="selectedRowKeys.length > 0">
      <a-menu slot="overlay">
        <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
      </a-menu>
      <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
    </a-dropdown> -->
    <!-- </div> -->

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div> -->

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="status" slot-scope="status, record">
          <a-tag color="red" v-if="record.status == 0">未签署</a-tag>
          <a-tag color="green" v-if="record.status == 1">专监签署</a-tag>
          <a-tag color="green" v-if="record.status == 2">总监签署</a-tag>
          <a-tag color="green" v-if="record.status == 3">施工单位签署</a-tag>
        </span>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
<!--              <a-menu-item>-->
<!--                <a @click="handleDetail(record)">详情</a>-->
<!--              </a-menu-item>-->
              <a-menu-item v-if ="record.contractid == null || record.contractid ==''  ">
                <a @click="print1(record.id)">指令单确认</a>
              </a-menu-item>
               <a-menu-item>
                 <a @click="print2(record.status,record.batchNo)">指令单签署</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <bhz-supervision-order-modal ref="modalForm" @ok="modalFormOk"></bhz-supervision-order-modal>
    <instruct-model ref="instruct" @change="getChange"></instruct-model>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BhzSupervisionOrderModal from './modules/BhzSupervisionOrderModal'
import InstructModel from './modules/InstructModel'
import { usershebeiList } from '@api/api'
import Vue from 'vue'
import {getAction} from "@api/manage";

export default {
  name: 'BhzSupervisionOrderList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BhzSupervisionOrderModal,
    InstructModel
  },
  data() {
    return {
      dictOption: [],
      description: 'bhz_supervision_order管理页面',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '编号',
          align: 'center',
          dataIndex: 'batchNo'
        },
        {
          title: '标段',
          align: 'center',
          dataIndex: 'departName'
        },

        {
          title: '设备编号',
          align: 'center',
          dataIndex: 'shebeiNo_dictText'
        },
        {
          title: '出料时间',
          align: 'center',
          dataIndex: 'productDatetime',
          // customRender:function (text) {
          //   return !text?"":(text.length>10?text.substr(0,10):text)
          // }
        },
        {
          title: '超标原因',
          align: 'center',
          dataIndex: 'overReason'
        },

        {
          title: '签收人',
          align: 'center',
          dataIndex: 'receiver'
        },
        {
          title: '预计处置时间',
          align: 'center',
          dataIndex: 'expectedDatetime',
          // customRender:function (text) {
          //   return !text?"":(text.length>10?text.substr(0,10):text)
          // }
        },
        {
          title: '监理单位',
          align: 'center',
          dataIndex: 'supervisionUnit'
        },
        {
          title: '施工单位',
          align: 'center',
          dataIndex: 'constructionUnit'
        },
        {
          title: '保存时间',
          align: 'center',
          dataIndex: 'saveDatetime',
          // customRender:function (text) {
          //   return !text?"":(text.length>10?text.substr(0,10):text)
          // }
        },
        //  {
        //   title: '状态',
        //   align: 'center',
        //   dataIndex: 'status',
        //   scopedSlots: { customRender: 'status' }
        // },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/bhzSupervisionOrder/bhzSupervisionOrder/list1',
        delete: '/bhzSupervisionOrder/bhzSupervisionOrder/delete',
        deleteBatch: '/bhzSupervisionOrder/bhzSupervisionOrder/deleteBatch',
        exportXlsUrl: '/bhzSupervisionOrder/bhzSupervisionOrder/exportXls',
        importExcelUrl: 'bhzSupervisionOrder/bhzSupervisionOrder/importExcel',
        siginzld:'/bhzSupervisionOrder/bhzSupervisionOrder/sign'
        // printUrl: 'jmreport/view/783131165361549312',
      },
      dictOptions: {},
      selectValue: '',
      superFieldList: [],
    }
  },
  created() {
    this.shebeiList()
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '0'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    print2: function (status,batchNo) {
      let  signurl = ""
      getAction(this.url.siginzld,{status:status,batchNo:batchNo}).then(res=>{
        if(res.code == 200){
          signurl = res.result
          console.log(res)
          window.open(signurl)
        }else{
          this.$message.warning(res.message);
          return;
        }

      })

    },

    print1(id) {//打印功能需要先去报表设计页面设计打印格式
      // if (this.selectedRowKeys.length !== 1) {
      //   let param = this.getQueryParams()
      //   console.log(param, '指令单')
      //   this.$message.error('请先选择一条指令单')
      // } else if (this.selectedRowKeys.length == 1) {//?paramsStr=${paramsStr}
      //   let param = this.getQueryParams()
      //   param['selections'] = this.selectedRowKeys.join(',')
      //   console.log(param, '指令单')
        // let url = `${window._CONFIG['domianURL']}/${this.url.printUrl}?id=${id}&token=${this.token}`
        // window.open(url)
      // }
      this.$refs.instruct.getList(id)
    },
    getChange(){
      this.loadData()
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'shebeiNo', text: '设备编号' })
      fieldList.push({ type: 'date', value: 'productDatetime', text: '出料时间' })
      fieldList.push({ type: 'string', value: 'overReason', text: '超标原因' })
      fieldList.push({ type: 'date', value: 'saveDatetime', text: '保存时间' })
      fieldList.push({ type: 'string', value: 'receiver', text: '签收人' })
      fieldList.push({ type: 'date', value: 'expectedDatetime', text: '预计处置时间' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>