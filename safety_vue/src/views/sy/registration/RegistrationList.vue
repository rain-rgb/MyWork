<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label=实验室名称>
              <a-input placeholder="请输入实验室名称" v-model="queryParam.sysName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="年份">
              <j-search-select-tag placeholder="请选择年份" :dictOptions="dictOption1"
                                   v-model="queryParam.year"></j-search-select-tag>
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
          <a-tag v-if="record.season == '1'">第一季度</a-tag>
          <a-tag v-if="record.season == '2'">第二季度</a-tag>
          <a-tag v-if="record.season == '3'">第三季度</a-tag>
          <a-tag v-if="record.season == '4'">第四季度</a-tag>
        </span>

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

    <registration-modal ref="modalForm" @ok="modalFormOk"></registration-modal>
  </a-card>
</template>

<script>

import '@assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import RegistrationModal from './modules/RegistrationModal'

export default {
  name: 'RegistrationList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    RegistrationModal
  },
  data() {
    return {
      description: 'registration管理页面',
      dictOption1: [],
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
          title: '实验室名称',
          align: 'center',
          dataIndex: 'sysName'
        },
        {
          title: '评分表名称',
          align: 'center',
          dataIndex: 'evaluationName'
        },
        {
          title: '年份',
          align: 'center',
          dataIndex: 'year'
        },
        {
          title: '季度',
          align: 'center',
          dataIndex: 'season',
          scopedSlots: { customRender: 'tags' }
        },
        {
          title: '人员管理',
          align: 'center',
          dataIndex: 'personnelNum'
        },
        {
          title: '仪器设备',
          align: 'center',
          dataIndex: 'shebeiNum'
        },
        {
          title: '实验环境',
          align: 'center',
          dataIndex: 'environmentNum'
        },
        {
          title: '检测行为',
          align: 'center',
          dataIndex: 'behaviorNum'
        },
        {
          title: '内业资料',
          align: 'center',
          dataIndex: 'informationNum'
        },
        {
          title: '体系管理',
          align: 'center',
          dataIndex: 'systemNum'
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
        list: '/pfdj/registration/list',
        delete: '/pfdj/registration/delete',
        deleteBatch: '/pfdj/registration/deleteBatch',
        exportXlsUrl: '/pfdj/registration/exportXls',
        importExcelUrl: 'pfdj/registration/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.initYear()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    initYear() {
      let yearn = (new Date()).getFullYear()
      let years = []
      for (let i = 0; i < 3; i++) {
        years.push({ text: yearn - i, value: yearn - i })
      }
      this.dictOption1 = years
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'sysName', text: '实验室名称' })
      fieldList.push({ type: 'string', value: 'evaluationName', text: '评分表名称' })
      fieldList.push({ type: 'string', value: 'year', text: '年份' })
      fieldList.push({ type: 'string', value: 'season', text: '季度' })
      fieldList.push({ type: 'string', value: 'personnelReason', text: '人员管理扣分原因' })
      fieldList.push({ type: 'int', value: 'personnelNum', text: '人员管理扣分数' })
      fieldList.push({ type: 'string', value: 'shebeiReason', text: '仪器设备扣分原因' })
      fieldList.push({ type: 'int', value: 'shebeiNum', text: '仪器设备扣分数' })
      fieldList.push({ type: 'string', value: 'environmentReason', text: '实验环境扣分原因' })
      fieldList.push({ type: 'int', value: 'environmentNum', text: '实验环境扣分数' })
      fieldList.push({ type: 'string', value: 'behaviorReason', text: '检测行为扣分原因' })
      fieldList.push({ type: 'int', value: 'behaviorNum', text: '检测行为扣分数' })
      fieldList.push({ type: 'string', value: 'informationReason', text: '内业资料扣分原因' })
      fieldList.push({ type: 'int', value: 'informationNum', text: '内业资料扣分数' })
      fieldList.push({ type: 'string', value: 'systemReason', text: '体系管理扣分原因' })
      fieldList.push({ type: 'int', value: 'systemNum', text: '体系管理扣分数' })
      fieldList.push({ type: 'date', value: 'dateTime', text: '数据时间' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>