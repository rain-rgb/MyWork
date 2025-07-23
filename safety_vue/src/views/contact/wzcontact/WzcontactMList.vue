<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="履约进度">
              <j-search-select-tag placeholder="请选择履约进度" v-model="queryParam.remarks" :dictOptions="dictOptions1">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="合同签订时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.signingDate_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.signingDate_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
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
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!--      <a-button type="primary" icon="printer" @click="print1">司磅单打印</a-button>-->
      <a-button type="primary" icon="download" @click="handleExportXls('供应方合同台账')">导出</a-button>
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

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

        <span slot="tags" slot-scope="tags, record">
          <a-tag v-if="record.remarks == '1'">正在履约</a-tag>
          <a-tag v-if="record.remarks == '2'">已退场，已封账</a-tag>
          <a-tag v-if="record.remarks == '3'">已退场，未封账</a-tag>
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
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
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

    <wzcontact-m-modal ref="modalForm" @ok="modalFormOk"></wzcontact-m-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import WzcontactMModal from './modules/WzcontactMModal'

export default {
  name: 'WzcontactMList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    WzcontactMModal
  },
  data() {
    return {
      selectValue: '',
      description: 'wzcontact_m管理页面',
      dictOptions1: [{
        text: '正在履约',
        value: '1'
      }, {
        text: '已退场，已封账',
        value: '2'
      }, {
        text: '已退场，未封账',
        value: '3'
      }],
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '公司名称',
          align: 'center',
          dataIndex: 'companyName'
        },
        {
          title: '准入证号',
          align: 'center',
          dataIndex: 'accessCertificateNumber'
        },
        {
          title: '代理人',
          align: 'center',
          dataIndex: 'agent'
        },
        {
          title: '联系方式',
          align: 'center',
          dataIndex: 'contactInformation'
        },
        {
          title: '合同摘要',
          align: 'center',
          dataIndex: 'contractSummary'
        },
        {
          title: '合同类别',
          align: 'center',
          dataIndex: 'contractCategory'
        },
        {
          title: '合同编号',
          align: 'center',
          dataIndex: 'contractNumber'
        },
        {
          title: '签订日期',
          align: 'center',
          dataIndex: 'signingDate',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '招标类型',
          align: 'center',
          dataIndex: 'biddingType'
        },
        {
          title: '合同金(元)',
          align: 'center',
          dataIndex: 'excludingTaxAmount'
        },
        {
          title: '增值税金(元)',
          align: 'center',
          dataIndex: 'valueAddedTax'
        },
        {
          title: '履约保证金(元)',
          align: 'center',
          dataIndex: 'paymentAmount'
        },
        {
          title: '履约进度',
          align: 'center',
          dataIndex: 'remarks',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '供应商编码',
          align: 'center',
          dataIndex: 'supplier'
        },
        // {
        //   title:'guid',
        //   align:"center",
        //   dataIndex: 'guid'
        // },
        // {
        //   title:'firstParty',
        //   align:"center",
        //   dataIndex: 'firstParty'
        // },
        {
          title: '合同文件',
          align: 'center',
          dataIndex: 'fileurl'
        },
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
        list: '/wzcontact/wzcontactM/list',
        delete: '/wzcontact/wzcontactM/delete',
        deleteBatch: '/wzcontact/wzcontactM/deleteBatch',
        exportXlsUrl: '/wzcontact/wzcontactM/exportXls',
        importExcelUrl: 'wzcontact/wzcontactM/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    print1: function () {//打印功能需要先去报表设计页面设计打印格式
      if (this.selectedRowKeys.length !== 1) {
        let param = this.getQueryParams()
        console.log(param, '打印信息')
        this.$message.error('请选择一条数据进行打印')
      } else if (this.selectedRowKeys.length == 1) {//?paramsStr=${paramsStr}
        let param = this.getQueryParams()
        param['selections'] = this.selectedRowKeys.join(',')
        console.log(param, '打印信息')
        let url = `${window._CONFIG['domianURL']}/${this.url.printUrl}?id=${param.selections}&token=${this.token}`
        window.open(url)
      }
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'companyName', text: '公司名称' })
      fieldList.push({ type: 'int', value: 'accessCertificateNumber', text: '准入证号' })
      fieldList.push({ type: 'string', value: 'agent', text: '代理人' })
      fieldList.push({ type: 'string', value: 'contactInformation', text: '联系方式' })
      fieldList.push({ type: 'string', value: 'contractSummary', text: '合同摘要' })
      fieldList.push({ type: 'string', value: 'contractCategory', text: '合同类别' })
      fieldList.push({ type: 'int', value: 'contractNumber', text: '合同编号(唯一码)' })
      fieldList.push({ type: 'date', value: 'signingDate', text: '签订日期' })
      fieldList.push({ type: 'string', value: 'biddingType', text: '招标类型' })
      fieldList.push({ type: 'string', value: 'excludingTaxAmount', text: '不含税合同价款金额（元）' })
      fieldList.push({ type: 'string', value: 'valueAddedTax', text: '增值税金额' })
      fieldList.push({ type: 'string', value: 'paymentAmount', text: '履约保证缴纳金额' })
      fieldList.push({ type: 'string', value: 'remarks', text: '履约进度，1、正在履约；2、已退场，已封账；3、已退场，未封账。' })
      fieldList.push({ type: 'string', value: 'supplier', text: '供应商编码' })
      fieldList.push({ type: 'string', value: 'guid', text: 'guid' })
      fieldList.push({ type: 'string', value: 'firstParty', text: 'firstParty' })
      fieldList.push({ type: 'string', value: 'fileurl', text: '合同文件' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>