<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    style="top:5%;height: 85%;overflow-y: hidden"
  >
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
      class="j-table-force-nowrap"
      @change="handleTableChange"
    >
      <template slot="htmlSlot" slot-scope="text">
        <div v-html="text"></div>
      </template>
      <template slot="wzcailiaonamedictManVoList" slot-scope="text, record">
        <a-select
          style="width: 100%;"
          placeholder="请选择规格型号"
          show-search
          allowClear
          option-filter-prop="children"
          @change="e=>handlerChangeSelect(e,record)"
        >
          <a-select-option :value="undefined">请选择</a-select-option>
          <a-select-option v-for="item in record.wzcailiaonamedictManVoList" :key="item.guigexinghao" >
            {{ item.guigexinghao }}
          </a-select-option>
        </a-select>
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
      <template slot="total" slot-scope="total, record">
        <a-input-number
          v-if="record.editable"
          style="margin: -5px 0;"
          :min="0"
          :max="record.erratumAfterQuantity * 1"
          :value="total"
          @change="e => handlerChange(e, record)"
        />
        <!-- @blur="e => handlerChange(e.target.value, record,1)"  和直接点击保存冲突 -->
        <template v-else>
          {{ total }}
        </template>
      </template>
      <template slot="upload" slot-scope="upload, record">
        <a-tag color="green" v-if="record.upload != ''">已上传</a-tag>
        <j-image-upload
          v-else-if="record.editable"
          class="avatar-uploader"
          text="上传"
          v-model="record.upload"
        ></j-image-upload>
        <a-tag color="yellow" v-else>未上传</a-tag>
      </template>
      <span slot="action" slot-scope="text, record">
        <a @click="handlerSaveNum(record)" v-if="record.editable">保存</a>
        <a @click="handlerEditNum(record)" v-else>编辑</a>

        <a-divider type="vertical" />
        <a-dropdown>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDeletes(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </a-dropdown>
      </span>
    </a-table>
  </a-modal>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { addRole, editRole, duplicateCheck } from '@/api/api'
import { deleteAction, postAction, getAction, putAction } from '@/api/manage'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import axios from 'axios'
export default {
  name: 'SemiFinishedProducts', //半成品对话框
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JselectDqDepart
  },
  props: ['dataSourceParaent', 'disableSubmitButton', 'currentDeptId'],
  data() {
    return {
      title: '操作',
      visible: false,
      roleDisabled: false,
      disableSubmit: false,
      model: {},
      layout: {
        labelCol: { span: 3 },
        wrapperCol: { span: 14 }
      },
      confirmLoading: false,
      validatorRules: {
        roleName: [
          { required: true, message: '请输入角色名称!' }
          // { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '请输入角色名称!' }
          // { min: 0, max: 64, message: '长度不超过 64 个字符', trigger: 'blur' },
          // { validator: this.validateRoleCode}
        ],
        description: [
          // { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
        ]
      },
      dataSource: [],
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
          title: '材料编号',
          align: 'center',
          dataIndex: 'subItemNumber'
        },
        {
          title: '材料名称',
          align: 'center',
          dataIndex: 'subItemName'
        },
        {
          title: '对应材料',
          align: 'center',
          width: 200,
          // dataIndex: 'wzcailiaonamedictManVoList',
          scopedSlots: { customRender: 'wzcailiaonamedictManVoList' }
        },

        {
          title: '设计数量',
          align: 'center',
          dataIndex: 'designQuantity'
        },
        {
          title: '复核数量',
          align: 'center',
          dataIndex: 'erratumAfterQuantity'
        },
        {
          title: '单位',
          align: 'center',
          dataIndex: 'unitName'
        },
        {
          title: '已使用数量',
          align: 'center',
          dataIndex: 'usedNumber'
        },

        {
          title: '本次使用数量',
          align: 'center',
          dataIndex: 'total',
          // width: 200,
          scopedSlots: { customRender: 'total' }
        },

        {
          title: '上传图片',
          align: 'center',
          dataIndex: 'upload',
          scopedSlots: { customRender: 'upload' }
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
        list: '',
        delete: '/rebarTaskChecklist/rebarTaskChecklist/delete',
        deleteBatch: '/rebarTaskChecklist/rebarTaskChecklist/deleteBatch',
        exportXlsUrl: '/rebarTaskChecklist/rebarTaskChecklist/exportXls',
        importExcelUrl: 'rebarTaskChecklist/rebarTaskChecklist/importExcel'
      },
      queryParam: {},
      rowData: {}
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    handlerChangeSelect(value, record) {
      let current=record.wzcailiaonamedictManVoList.find(x=>x.guigexinghao==value)
      record.cailiaoName=current.cailiaoName
      record.cailiaoNo=current.cailiaoNo
      record.guid=current.guid
      record.guigexinghao=current.guigexinghao

    },
    handleDeletes(id) {
      const newData = [...this.dataSource]
      this.dataSource = newData.filter(x => x.id != id)
    },
    handlerChange(value, record) {
      const newData = [...this.dataSource]
      let current = newData.find(x => x.id == record.id)
      if (current) {
        current.total = value
        this.dataSource = newData
      }
    },
    handlerSaveNum(record) {
      const newData = [...this.dataSource]
      let current = newData.find(x => x.id == record.id)
      if (current) {
        delete current.editable
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
    async add(rowData) {
      this.visible = true
      this.rowData = rowData
      this.dataSource = []
      await getAction('/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/getToken', {}).then(res => {
        let token = res.result
        getAction('/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/wzCailiao', {
          wbsId: rowData.value,
          sysOrgCodes: this.rowData.orgCodes,
          sysOrgCode: this.$store.state.user.info.orgCode,
          token
        }).then(res => {
          this.dataSource = res.result.map(x => ({ ...x, total: 0, upload: '', editable: false }))
        })
      })
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
      //编辑页面禁止修改角色编码
      if (this.model.id) {
        this.roleDisabled = true
      } else {
        this.roleDisabled = false
      }
    },
    close() {
      // this.$refs.form.clearValidate();
      // this.$emit('close');
      this.visible = false
      // this.dataSource=[]
    },
    handleOk() {
      postAction('/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/addWzCaiLiaoComponentTask', {
        number0CaiLiaoList: this.dataSource,
        sysOrgCodes: this.rowData.orgCode,
        sysOrgCode: this.$store.state.user.info.orgCode
      }).then(res => {})
      this.$emit('handlerOk', this.selectionRows)
      this.visible = false
      const vm = this
      this.$confirm({
        title: '任务启动成功，是否跳转到构件任务管理查看?',
        onOk() {
          vm.$router.push({ path: '/rebar/taskManagement' })
        },
        onCancel() {}
      })
    },
    handleCancel() {
      this.close()
    },
    validateRoleCode(rule, value, callback) {
      if (/[\u4E00-\u9FA5]/g.test(value)) {
        callback('角色编码不可输入汉字!')
      } else {
        let params = {
          tableName: 'sys_role',
          fieldName: 'role_code',
          fieldVal: value,
          dataId: this.model.id
        }
        duplicateCheck(params).then(res => {
          if (res.success) {
            callback()
          } else {
            callback(res.message)
          }
        })
      }
    }
  }
}
</script>

<style scoped></style>
