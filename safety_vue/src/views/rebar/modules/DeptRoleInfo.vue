<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <!-- <div class="table-page-search-wrapper">
      搜索区域
      <a-form layout="inline">
        <a-row :gutter="10">
          <a-col :md="10" :sm="12">
            <a-form-item label="部门角色名称" style="margin-left:8px">
              <a-input placeholder="请输入部门角色" v-model="queryParam.roleName"></a-input>
            </a-form-item>
          </a-col>
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery" icon="search" style="margin-left: 18px">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </a-col>
          </span>
        </a-row>
      </a-form>
    </div> -->
    <!-- 操作按钮区域 -->
    <div class="table-operator" :md="24" :sm="24">
      <a-button @click="handleAdd" type="primary" icon="plus" :disabled="!rowData.isLast">添加</a-button>
      <a-button @click="handlerStartTask" type="primary" icon="plus" :disabled="!rowData.isLast">启动任务</a-button>
      <a-button @click="handlerStartTask2" type="primary" icon="plus" :disabled="!rowData.isLast">半成品加工</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
      </a-dropdown>
    </div>
    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600"> {{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <!-- <span slot="componentNumber" slot-scope="age, record, index">
          <input type="text" v-model="dataSource[index].componentNumber" />
        </span> -->
        <template slot="componentNumber" slot-scope="componentNumber, record">
          <a-input
            v-if="record.editable"
            style="margin: -5px 0;"
            :value="componentNumber"
            @change="e => handlerChange(e.target.value, record)"
          />
          <!-- @blur="e => handlerChange(e.target.value, record,1)"  和直接点击保存冲突 -->
          <template v-else>
            {{ componentNumber }}
          </template>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handlerSaveNum(record)" v-if="record.editable">保存</a>
          <a @click="handlerEditNum(record)" v-else>编辑数量</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete1(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->
    <!-- 表单区域 -->
    <!-- <sys-depart-role-modal ref="modalForm" @ok="modalFormOk"/> -->
    <!-- <dept-role-auth-modal ref="modalDeptRole" /> -->
    <!-- <ingredient-management-add-edit ref="modalForm" @ok="modalFormOk"></ingredient-management-add-edit> -->
    <task-modal-add-component
      ref="modalForm"
      @ok="modalFormOk"
      @handlerOk="handlerOk"
      :currentDeptId="currentDeptId"
    ></task-modal-add-component>
    <task-modal-add-edit ref="modalForm1" @handlerOk1="handlerOk1"></task-modal-add-edit>
    <SemiFinishedProducts ref="SemiFinishedProducts" />
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction } from '@/api/manage'
import taskModalAddComponent from './taskModalAddComponent'
import taskModalAddEdit from './taskModalAddEdit'
import SemiFinishedProducts from './SemiFinishedProducts'
import { deleteAction, postAction, putAction } from '@/api/manage'
import { mapState } from 'vuex'
// import SysDepartRoleModal from './SysDepartRoleModal'
// import DeptRoleAuthModal from './DeptRoleAuthModal'

export default {
  name: 'DeptRoleInfo',
  // components: { DeptRoleAuthModal, SysDepartRoleModal },
  mixins: [JeecgListMixin],
  components: {
    taskModalAddComponent,
    taskModalAddEdit,
    SemiFinishedProducts
  },
  data() {
    return {
      description: '部门角色信息',
      currentDeptId: '',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '构件编号',
          align: 'center',
          dataIndex: 'componentId'
        },

        {
          title: '构件名称',
          align: 'center',
          dataIndex: 'componentName'
        },

        {
          title: '规格型号',
          align: 'center',
          dataIndex: 'componentModel'
        },
        {
          title: '单位',
          align: 'center',
          dataIndex: 'unit_dictText'
        },

        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark'
        },
        {
          title: '数量',
          align: 'center',
          dataIndex: 'componentNumber',
          width: 150,
          scopedSlots: {
            customRender: 'componentNumber'
          }
        },
        {
          title: '操作',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/rebarComponent/rebarComponent/list',
        delete: '/rebarComponent/rebarComponent/delete',
        deleteBatch: '/rebarComponent/rebarComponent/deleteBatch'
      },
      rowData: {},
      num: 1
    }
  },

  methods: {
    handlerOk1(obj) {
      console.log(obj, '传递过来的obj')
      obj.rebarComponentTaskList = this.selectionRows
      obj.orgCodes = this.rowData.orgCode
      obj.isDeleted = 0
      obj.status = 0
      obj.orgCode = this.$store.state.user.info.orgCode
      // this.sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
      postAction('/rebarTaskChecklist/rebarTaskChecklist/addTaskCheck', obj).then(res => {
        console.log(res, 'ressss')
      })
    },
    handlerStartTask() {
      if (this.selectionRows.length <= 0) {
        this.$message.error('请在列表中选择一个构件!')
        return
      }
      // 检查 selectionRows 是否是数组
      if (!Array.isArray(this.selectionRows)) {
        this.$message.error('选择的构件无效!')
        return
      }
      this.$refs.modalForm1.title = '启动任务'
      this.$refs.modalForm1.add(this.selectionRows)
    },
    handlerStartTask2() {
      this.$refs.SemiFinishedProducts.title = '半成品加工'
      this.$refs.SemiFinishedProducts.add(this.rowData)
    },
    handlerSaveNum(record) {
      const newData = [...this.dataSource]
      let current = newData.find(x => x.id == record.id)
      if (current) {
        putAction('/rebarComponent/rebarComponent/edit', current).then(res => {
          delete current.editable
          this.dataSource = newData
        })
      }
    },
    handlerChange(value, record, type = 0) {
      const newData = [...this.dataSource]
      let current = newData.find(x => x.id == record.id)
      if (current) {
        current.componentNumber = value
        if (type == 1) delete current.editable
        this.dataSource = newData
      }
    },
    handlerEditNum(record) {
      const newData = [...this.dataSource]
      let current = newData.find(x => x.id == record.id)
      if (current) {
        current.editable = true
        this.dataSource = newData
      }
    },
    handlerOk(selectedRowKeys) {
      // selectedRowKeys.forEach(item=>item.)
      postAction('/rebarComponent/rebarComponent/addDepartComponent', {
        rebarComponents: selectedRowKeys,
        orgCodes: this.currentDeptId
      }).then(res => {
        this.loadData(1)
      })
    },
    handleOpen(record) {
      console.log(record, 'record')
      this.rightcolval = 1
      this.selectedRowKeys1 = [record.id]
      this.model1 = Object.assign({}, record)
      this.currentRoleId = record.componentId
      this.onClearSelected2()
      this.loadData2()
    },
    handleDelete1: function(id) {
      this.handleDelete(id)
      this.dataSource2 = []
      this.currentRoleId = ''
    },
    searchReset() {
      this.queryParam = {}
      this.loadData(1)
    },
    loadData(arg) {
      if (!this.url.list) {
        this.$message.error('请设置url.list属性!')
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      let params = this.getQueryParams() //查询条件
      params.orgCodes = this.currentDeptId
      if (this.num > 1) {
        getAction(this.url.list, params).then(res => {
          if (res.success && res.result) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
        })
      }
      this.num++
    },
    open(record) {
      this.rowData = { ...record }
      this.currentDeptId = record.orgCode
      this.loadData(1)
    },
    clearList() {
      this.currentDeptId = ''
      this.dataSource = []
    },
    hasSelectDept() {
      if (this.currentDeptId == '') {
        this.$message.error('请选择一个部门!')
        return false
      }
      return true
    },
    handleEdit: function(record) {
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.departDisabled = true
      this.$refs.modalForm.disableSubmit = false
      this.$refs.modalForm.edit(record, record.departId)
    },
    handleAdd: function() {
      if (this.currentDeptId == '') {
        this.$message.error('请选择一个部门!')
      } else {
        this.$refs.modalForm.departDisabled = true
        this.$refs.modalForm.add(this.currentDeptId)
        this.$refs.modalForm.title = '新增'
      }
    },
    handlePerssion: function(record) {
      this.$refs.modalDeptRole.show(record.id, record.departId)
    }
  }
}
</script>

<style scoped></style>
