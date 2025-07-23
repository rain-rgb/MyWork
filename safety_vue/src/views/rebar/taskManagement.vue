<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">


          <a-col :span="4">
            <a-form-item label="开始时间">
              <j-date placeholder="开始测量时间" v-model="queryParam.createTime_begin" :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="结束时间">
              <j-date placeholder="结束测量时间" v-model="queryParam.createTime_end" :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss" class="test" />
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="状态">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.status"
                dictCode="rebar_use"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="施工部位">
              <!-- <JselectDqDepart v-model="queryParam.sys_depart_orgcode" /> -->
              <JSelectDqProjName v-model="queryParam.orgCodes" ::multi="false" />
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" icon="download" @click="handleExportXls('构件任务管理')"
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
          <!-- <a @click="handleEdit(record)">编辑</a> -->
          <a @click="handlerMartical(record)">材料</a>

          <a-divider type="vertical" />
          <a @click="handlerGetMartical(record)" v-if="record.status == 0">领料</a>
          <a @click="handlerStatus(record, 2)" v-else-if="record.status == 1">加工</a>
          <a @click="handlerStatus(record, 3)" v-else-if="record.status == 2">完成</a>
          <!-- <a @click="handlerStatus(record, 6)" v-else>生产</a> -->


          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>

          <!-- <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>

              </a-menu-item>
              <a-menu-item v-if="record.status == 0">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleDetail(record)">材料</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleDetail(record)">打印</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown> -->
        </span>
        <!-- <template slot="status" slot-scope="text, record">
          <span v-if="record.status == 0">待分配</span>
          <span v-if="record.status == 1">代加工</span>
          <span v-if="record.status == 2">加工中</span>
          <span v-if="record.status == 3">加工完成</span>
        </template> -->

      </a-table>
    </div>

    <task-modal-add-edit ref="modalForm" @ok="modalFormOk"></task-modal-add-edit>
    <task-material-detail ref="taskMaterialDetail"></task-material-detail>
    <task-material-get ref="taskMaterialGet" @ok="loadData(1)"></task-material-get>
  </a-card>
</template>

<script>
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import taskModalAddEdit from './modules/taskModalAddEdit'
import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
import { deleteAction, postAction, getAction, putAction } from '@/api/manage'
import taskMaterialDetail from './modules/taskMaterialDetail';
import taskMaterialGet from './modules/taskMaterialGet';
export default {
  name: 'taskManagement',//构件任务管理
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    taskModalAddEdit,
    JselectDqDepart,
    JSelectDqProjName,
    taskMaterialDetail,
    taskMaterialGet
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
          title: '任务编号',
          align: "center",
          dataIndex: 'taskId'
        },
        {
          title: '施工部位',
          align: "center",
          dataIndex: 'orgCodes_dictText'
        },
        {
          title: '班组长',
          align: "center",
          dataIndex: 'teamLeader'
        },
        {
          title: '使用时间',
          align: "center",
          dataIndex: 'useTime'
        },
        {
          title: '申请人',
          align: "center",
          dataIndex: 'applicant'
        },
        {
          title: '状态',
          align: "center",
          dataIndex: 'status_dictText',
          // scopedSlots: { customRender: 'status' }
        },
        {
          title: '备注',
          align: "center",
          dataIndex: 'remark'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: "/rebarTaskChecklist/rebarTaskChecklist/list",
        delete: "/rebarTaskChecklist/rebarTaskChecklist/deleteTaskAndComponent",
        deleteBatch: "/rebarTaskChecklist/rebarTaskChecklist/deleteBatch",
        exportXlsUrl: "/rebarTaskChecklist/rebarTaskChecklist/exportXls",
        importExcelUrl: "rebarTaskChecklist/rebarTaskChecklist/importExcel",

      },
      dictOptions: {},
      superFieldList: [],
      queryParam: {
        isDeleted: 0,
      },
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
    handlerGetMartical(record){
      this.$refs.taskMaterialGet.visible = true
      this.$refs.taskMaterialGet.loadDataTable(record)
      this.$refs.taskMaterialGet.loadDataTable(record)
    },
    handlerMartical(record) {
      this.$refs.taskMaterialDetail.visible = true
      this.$refs.taskMaterialDetail.loadDataTable(record)

    },
    handlerStatus(record, status) {
      putAction('/rebarTaskChecklist/rebarTaskChecklist/edit', { id: record.id, status }).then(res => {
        this.loadData()
      })
    },
    handleDetail: function (record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.getBootomData(record);
      this.$refs.modalForm.isDetail = true;
      this.$refs.modalForm.title = "详情";
      this.$refs.modalForm.disableSubmit = true;
    },
    handleEdit: function (record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.getBootomData(record);
      this.$refs.modalForm.title = "编辑";
      this.$refs.modalForm.disableSubmit = false;
    },
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