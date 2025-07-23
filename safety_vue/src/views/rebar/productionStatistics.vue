<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">


          <a-col :span="4">
            <a-form-item label="开始时间">
              <j-date placeholder="开始测量时间" v-model="queryParam.startDate" :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="结束时间">
              <j-date placeholder="结束测量时间" v-model="queryParam.endDate" :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss" class="test" />
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" icon="download" @click="handleExportXls('构件生产统计')"
                style="margin-left: 8px">导出</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('rebar_component_task')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
        @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <j-super-query :fieldList="superFieldList" ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div> -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
          selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table ref="table" size="middle" :scroll="{ x: true }" bordered rowKey="id" :columns="columns"
        :dataSource="dataSource" :pagination="ipagination" :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" class="j-table-force-nowrap"
        @change="handleTableChange">


        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
            style="max-width:80px;font-size: 12px;font-style: italic;" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
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

    <!-- <rebar-component-task-modal ref="modalForm" @ok="modalFormOk"></rebar-component-task-modal> -->
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
// import RebarComponentTaskModal from './modules/RebarComponentTaskModal'

export default {
  name: 'AcceptanceAndDelivery',//构件生产统计
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    // RebarComponentTaskModal
  },
  data() {
    return {
      description: 'rebar_component_task管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '所属单位(机构)',
          align: "center",
          dataIndex: 'orgCode_dictText'
        },
        {
          title: '部位',
          align: "center",
          dataIndex: 'orgCodes_dictText'
        },
        {
          title: '构件编号',
          align: "center",
          dataIndex: 'componentId'
        },
        {
          title: '构件名称',
          align: "center",
          dataIndex: 'componentName'
        },
        {
          title: '规格型号',
          align: "center",
          dataIndex: 'componentModel'
        },
        {
          title: '数量',
          align: "center",
          dataIndex: 'componentNumber'
        },
        {
          title: '重量kg',
          align: "center",
          dataIndex: 'weight'
        },
        {
          title: '总重量kg',
          align: "center",
          dataIndex: 'totalWeight'
        },
        {
          title: '原料消耗kg',
          align: "center",
          dataIndex: 'materialTotalWeight'
        },

      ],
      url: {
        list: "/rebarComponent/rebarComponent/componentList",
        delete: "/rebarComponentTask/rebarComponentTask/delete",
        deleteBatch: "/rebarComponentTask/rebarComponentTask/deleteBatch",
        // exportXlsUrl: "/rebarComponent/rebarComponent/exportXls",
        exportXlsUrl: "/rebarComponent/rebarComponent/componentExportXls",
        importExcelUrl: "rebarComponentTask/rebarComponentTask/importExcel",

      },
      dictOptions: {},
      superFieldList: [],
      queryParam: {status:2},
    }
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: 'string', value: 'componentId', text: '构件主键id' })
      fieldList.push({ type: 'string', value: 'taskId', text: '任务清单主键id' })
      fieldList.push({ type: 'int', value: 'designComponentNumber', text: '设计构件数量' })
      fieldList.push({ type: 'int', value: 'actualComponentNumber', text: '领用构件数量' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>