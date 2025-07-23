<template>
  <a-row :gutter="10">
    <a-col :md="leftColMd" :sm="24" style="margin-bottom: 20px">
      <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :xl="6" :lg="7" :md="8" :sm="24">
                <a-form-item label="设备编号">
                  <a-input placeholder="请输入设备编号" v-model="queryParam.sbjno"></a-input>
                </a-form-item>
              </a-col>
              <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
              </a-col>
            </a-row>
          </a-form>
        </div>
        <!-- 查询区域-END -->

        <!-- 操作按钮区域 -->
        <div class="table-operator">
<!--          <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--          <a-button type="primary" icon="download" @click="handleExportXls('ai视频设备任务id配置表')">导出</a-button>-->
<!--          <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader"-->
<!--                    :action="importExcelUrl"-->
<!--                    @change="handleImportExcel">-->
<!--            <a-button type="primary" icon="import">导入</a-button>-->
          </a-upload>
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
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type:'radio'}"
            class="j-table-force-nowrap"
            @change="handleTableChange"
            :customRow="handleClick"
            :rowClassName="setRowClassName"
            >
            <span slot="tags" slot-scope="tags, record">
              <a-tag color="orange" v-if="record.shebeiStatus == '0'">待审核</a-tag>
              <a-tag color="green" v-if="record.shebeiStatus == '1'">已审核</a-tag>
              <a-tag color="red" v-if="record.shebeiStatus == '2'">已驳回</a-tag>
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
<!--          <a @click="handleEdit(record)">编辑</a>-->
              <a @click="handleOpen(record)">任务配置</a>
              <!--          <a-divider type="vertical"/>-->
              <!--          <a-dropdown>-->
              <!--            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>-->
              <!--            <a-menu slot="overlay">-->
              <!--              <a-menu-item>-->
              <!--                <a @click="handleDetail(record)">详情</a>-->
              <!--              </a-menu-item>-->
              <!--              <a-menu-item>-->
              <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
              <!--                  <a>删除</a>-->
              <!--                </a-popconfirm>-->
              <!--              </a-menu-item>-->
              <!--            </a-menu>-->
              <!--          </a-dropdown>-->
            </span>
          </a-table>
        </div>
      </a-card>
    </a-col>
    <a-col :md="rightColMd" :sm="24" v-if="this.rightcolval === 1">
      <a-card :bordered="false">
        <div style="text-align: right;">
          <a-icon type="close-circle" @click="hideUserList"/>
        </div>
        <!-- 查询区域 -->
        <!--        <div class="table-page-search-wrapper">-->
        <!--          <a-form layout="inline">-->
        <!--            <a-row :gutter="24">-->

        <!--              <a-col :md="12" :sm="12">-->
        <!--                <a-form-item label="用户账号">-->
        <!--                  <a-input placeholder="" v-model="queryParam1.username"></a-input>-->
        <!--                </a-form-item>-->
        <!--              </a-col>-->
        <!--              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">-->
        <!--            <a-col :md="9" :sm="24">-->
        <!--             <a-button type="primary" @click="searchQuery1" icon="search" style="margin-left: 21px">查询</a-button>-->
        <!--              <a-button type="primary" @click="searchReset1" icon="reload" style="margin-left: 8px">重置</a-button>-->

        <!--            </a-col>-->
        <!--          </span>-->
        <!--            </a-row>-->
        <!--          </a-form>-->
        <!--        </div>-->
        <!-- 操作按钮区域 -->
        <div class="table-operator" :md="24" :sm="24">
          <a-button @click="handleAdd1" type="primary" icon="plus" style="margin-top: 16px">新增任务</a-button>
          <!--<a-button @click="handleEdit2" type="primary" icon="edit" style="margin-top: 16px">用户编辑</a-button>-->
          <!--          <a-button @click="handleAddUserRole" type="primary" icon="plus" style="margin-top: 16px">已有用户</a-button>-->

          <!--          <a-dropdown v-if="selectedRowKeys1.length > 0">-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item key="1" @click="batchDel1">-->
          <!--                <a-icon type="delete"/>-->
          <!--                删除-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--            <a-button style="margin-left: 8px"> 批量操作-->
          <!--              <a-icon type="down"/>-->
          <!--            </a-button>-->
          <!--          </a-dropdown>-->
        </div>
        <!-- table区域-begin -->
        <div>
<!--          <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">-->
<!--            <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{-->
<!--              selectedRowKeys1.length-->
<!--            }}</a>项-->
<!--            <a style="margin-left: 24px" @click="onClearSelected1">清空</a>-->
<!--          </div>-->
          <a-table
            ref="table2"
            bordered
            size="middle"
            rowKey="id"
            :columns="columns1"
            :dataSource="dataSource1"
            :pagination="ipagination1"
            :loading="loading1"
            :rowSelection="{selectedRowKeys: selectedRowKeys1, onChange: onSelectChange1}"
            @change="handleTableChange1">
           <span slot="action" slot-scope="text, record">
           <a @click="handleEdit1(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete1(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
          </a-table>
        </div>
        <!-- 表单区域 -->
        <!-- 右侧APP授权表单区域 -->
        <ai-warn-msg-cfg-modal ref="modalForm" @ok="modalFormOk"></ai-warn-msg-cfg-modal>
<!--                <Select-User-Modal ref="selectUserModal" @selectFinished="selectOK"></Select-User-Modal>-->
      </a-card>
    </a-col>
  </a-row>

</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import AiWarnMsgCfgModal from './modules/AiWarnMsgCfgModal'
import { deleteAction, getAction } from '@api/manage'

export default {
  name: 'AiWarnMsgCfgList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    AiWarnMsgCfgModal
  },
  data() {
    return {
      description: 'ai视频设备任务id配置表管理页面',
      rightcolval: 0,
      selectedRowKeys1: [],
      selectionRows1: [],
      currentRoleId: '',
      queryParam1: {},
      // 表头
      dataSource1: [],
      columns1: [
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
          title: '任务id',
          align: 'center',
          dataIndex: 'cid'
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeiid_dictText'
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy_dictText'
        },
        {
          title: '创建日期',
          align: 'center',
          dataIndex: 'createTime',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          }
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
      ipagination1: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      isorter1: {
        column: 'createTime',
        order: 'desc'
      },
      loading1: false,
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
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy_dictText'
        },
        {
          title: '创建日期',
          align: 'center',
          dataIndex: 'createTime',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '所属部门',
          align: 'center',
          dataIndex: 'sysOrgCode_dictText',

        },
        {
          title: '设备类型',
          align: 'center',
          dataIndex: 'sbtype_dictText',
        },
        {
          title: '设备编号',
          align: 'center',
          dataIndex: 'sbjno'
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'sbname'
        },
        {
          title: '设备简称',
          align: 'center',
          dataIndex: 'sbjsimplename'
        },
        {
          title: '设备状态',
          align: 'center',
          dataIndex: 'shebeiStatus',
          key: 'shebeiStatus',
          scopedSlots: { customRender: 'tags' },
        },
        // {
        //   title: '更新人',
        //   align: "center",
        //   dataIndex: 'updateBy_dictText'
        // },
        // {
        //   title: '更新日期',
        //   align: "center",
        //   dataIndex: 'updateTime',
        //   customRender: function (text) {
        //     return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
        //   }
        // },
        // {
        //   title: '审核人',
        //   align: "center",
        //   dataIndex: 'reviewBy_dictText'
        // },
        // {
        //   title: '审核日期',
        //   align: "center",
        //   dataIndex: 'reviewTime',
        //   customRender: function (text) {
        //     return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
        //   }
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
        list: '/system/shebeiInfo/ailist',
        delete: '/aiwarnmsgcfg/aiWarnMsgCfg/delete',
        deleteBatch: '/aiwarnmsgcfg/aiWarnMsgCfg/deleteBatch',
        exportXlsUrl: '/aiwarnmsgcfg/aiWarnMsgCfg/exportXls',
        importExcelUrl: 'aiwarnmsgcfg/aiWarnMsgCfg/importExcel',
        list1: '/aiwarnmsgcfg/aiWarnMsgCfg/list'

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
    leftColMd() {
      return this.selectedRowKeys.length === 0 ? 24 : 12
    },
    rightColMd() {
      return this.selectedRowKeys.length === 0 ? 0 : 12
    }
  },
  methods: {
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'cid', text: '任务id', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebeiid', text: '设备id 关联表shebei_info的id', dictCode: '' })
      this.superFieldList = fieldList
    },
    handleOpen(record) {
      //console.log('record', record)
      this.rightcolval = 1
      this.selectedRowKeys = [record.id]
      this.currentRoleId = record.sbjno
      this.onClearSelected1()
      this.loadData1()
    },
    loadData1(arg) {
      if (!this.url.list1) {
        this.$message.error('请设置url.list1属性!')
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination1.current = 1
      }
      if (this.currentRoleId === '') return
      // let params = this.getQueryParams2()//查询条件
      // params.roleId = this.currentRoleId
      let params = {
        shebeiid: this.currentRoleId,
        pageNo: this.ipagination1.current,
        pageSize: this.ipagination1.pageSize
      }
      this.loading1 = true
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          this.dataSource1 = res.result.records
          this.ipagination1.total = res.result.total
        }
        console.log('this.dataSource1', this.dataSource1)
        this.loading1 = false
      })

    },
    hideUserList() {
      this.rightcolval = 0
      this.selectedRowKeys = []
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      this.rightcolval = 1
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectionRows
      this.currentRoleId = selectedRowKeys[0]
      this.loadData1()
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
    onClearSelected1() {
      this.selectedRowKeys1 = []
      this.selectionRows1 = []
    },
    onSelectChange1(selectedRowKeys, selectionRows) {
      this.selectedRowKeys1 = selectedRowKeys
      this.selectionRows1 = selectionRows
    },
    handleTableChange1(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter1.column = sorter.field
        this.isorter1.order = 'ascend' == sorter.order ? 'asc' : 'desc'
      }
      this.ipagination1 = pagination
      this.loadData1()
    },
    handleEdit1: function (record) {
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.disableSubmit = false
      this.$refs.modalForm.edit(record)
    },
    handleAdd1: function () {
      if (this.currentRoleId == '') {
        this.$message.error('请选择一个角色!')
      } else {
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit({shebeiid:this.currentRoleId});
      }
    },
    handleDelete1: function (id) {
      // this.handleDelete(id)
      // this.dataSource2 = []
      // this.currentRoleId = ''
    },
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>