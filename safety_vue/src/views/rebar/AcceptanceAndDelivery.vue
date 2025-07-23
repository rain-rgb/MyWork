<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="4">
            <a-form-item label="开始时间">
              <j-date
                placeholder="开始测量时间"
                v-model="queryParam.createTime_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="结束时间">
              <j-date
                placeholder="结束测量时间"
                v-model="queryParam.createTime_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
                class="test"
              />
            </a-form-item>
          </a-col>
          <!-- <a-col :span="4">
            <a-form-item label="状态">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.status" dictCode="rebar_task_status"></j-dict-select-tag>
            </a-form-item>
          </a-col> -->
          <a-col :span="4">
            <a-form-item label="施工部位">
              <!-- <JselectDqDepart v-model="queryParam.sys_depart_orgcode" /> -->
              <JSelectDqProjName v-model="queryParam.orgCodes" />
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="状态">
              <j-dict-select-tag
                placeholder="请选择"
                v-model="queryParam.status"
                dictCode="rebar_acceptance_status"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" icon="download" @click="handleExportXls('构件验收出库')" style="margin-left: 8px"
                >导出</a-button
              >
              <a-button
                @click="printXls"
                v-has="'shigongphb:print'"
                type="primary"
                icon="printer"
                style="margin-left: 8px"
                >出库码</a-button
              >
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
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width:80px;font-size: 12px;font-style: italic;"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a
            v-has="'AcceptanceAndDelivery:yanshou'"
            @click="handleDetail1(record, '验收')"
            :disabled="!(record.status == 3 || record.status == 6)"
            >验收</a
          >
          <a-divider v-has="'AcceptanceAndDelivery:yanshou'" type="vertical" />
          <a @click="handleDetail(record, '出库')" :disabled="!(record.status == 4 || record.status == 6)">出库</a>
          <a-divider  type="vertical" />
          <a @click="handleDetail1(record, '查看')">查看</a>

          <!-- <a-divider type="vertical" /> -->
          <!-- <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleEdit(record)">编辑</a>

              </a-menu-item>
              <a-menu-item>
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

    <acceptance-and-delivery-detail ref="modalForm" @ok="modalFormOk"></acceptance-and-delivery-detail>
    <acceptance-and-delivery-yan-shou
      ref="acceptanceAndDeliveryYanShou"
      @ok="handlerAcceptanceAndDeliveryYanShou"
    ></acceptance-and-delivery-yan-shou>
  </a-card>
</template>

<script>
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import acceptanceAndDeliveryDetail from './modules/acceptanceAndDeliveryDetail'
import acceptanceAndDeliveryYanShou from './modules/acceptanceAndDeliveryYanShou'
import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
export default {
  name: 'AcceptanceAndDelivery', //构件验收出库
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    acceptanceAndDeliveryDetail,
    JselectDqDepart,
    acceptanceAndDeliveryYanShou,
    JSelectDqProjName
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
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '任务编号',
          align: 'center',
          dataIndex: 'taskId'
        },
        {
          title: '施工部位',
          align: 'center',
          dataIndex: 'orgCodes_dictText'
        },
        {
          title: '班组长',
          align: 'center',
          dataIndex: 'teamLeader'
        },
        {
          title: '使用时间',
          align: 'center',
          dataIndex: 'useTime'
        },
        {
          title: '申请人',
          align: 'center',
          dataIndex: 'applicant'
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status_dictText'
          // scopedSlots: { customRender: 'status' }
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark'
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
        list: '/rebarTaskChecklist/rebarTaskChecklist/list2',
        delete: '/rebarTaskChecklist/rebarTaskChecklist/deleteTaskAndComponent',
        deleteBatch: '/rebarTaskChecklist/rebarTaskChecklist/deleteBatch',
        exportXlsUrl: '/rebarTaskChecklist/rebarTaskChecklist/exportYanShouChuKuXls', //导出
        importExcelUrl: 'rebarTaskChecklist/rebarTaskChecklist/importExcel',
        printUrl: 'jmreport/view/841898669508464640'
      },
      dictOptions: {},
      superFieldList: [],
      queryParam: {
        isDeleted: 0
      }
    }
  },
  created() {
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    handlerAcceptanceAndDeliveryYanShou() {
      this.loadData(1)
    },
    handleDetail: function(record, title) {
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.getBootomData(record)
      this.$refs.modalForm.isDetail = true
      this.$refs.modalForm.title = title
      this.$refs.modalForm.disableSubmit = true
    },
    handleDetail1: function(record, title) {
      if (title == '查看') {
        this.$refs.acceptanceAndDeliveryYanShou.disableSubmit = true
      } else {
        this.$refs.acceptanceAndDeliveryYanShou.disableSubmit = false
      }
      this.$refs.acceptanceAndDeliveryYanShou.edit(record)
      this.$refs.acceptanceAndDeliveryYanShou.getBootomData(record)
      this.$refs.acceptanceAndDeliveryYanShou.isDetail = true
      this.$refs.acceptanceAndDeliveryYanShou.title = title
    },
    handleEdit: function(record) {
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.getBootomData(record)
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.disableSubmit = false
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
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
