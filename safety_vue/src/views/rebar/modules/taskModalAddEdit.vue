<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk(0)"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    style="top:5%;height: 85%;overflow-y: hidden"
  >
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" v-bind="layout" :model="model" :rules="validatorRules" :labelCol="{ span: 6 }">
        <!-- required -->
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="申请人" prop="applicant">
              <a-input v-model="model.applicant" :disabled="disableSubmit" placeholder="请输入申请人" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="申请单位" prop="applicantUnit">
              <a-input v-model="model.applicantUnit" :disabled="disableSubmit" placeholder="请输入申请单位" />
            </a-form-model-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="12">
            <a-form-model-item label="加工厂" prop="factoryId">
              <a-select
                v-model="model.factoryId"
                placeholder="请选择加工厂"
                show-search
                allowClear
                option-filter-prop="children"
                @change="handlerChangeSelect"
                style="width: 100%;"
              >
                <a-select-option :value="undefined">请选择</a-select-option>
                <a-select-option v-for="item in jiagongchang" :key="`${item.factoryId}`">
                  {{ item.factoryName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="班组长" prop="teamLeaderId">
              <a-select
                v-model="model.teamLeaderId"
                placeholder="请选择班组长"
                show-search
                allowClear
                option-filter-prop="children"
                style="width: 100%;"
              >
                <a-select-option :value="undefined">请选择</a-select-option>
                <a-select-option v-for="item in banzuzhang" :key="`${item.teamLeaderId}`">
                  {{ item.teamLeaderName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="使用时间" prop="useTime">
              <j-date
                placeholder="请选择使用时间"
                v-model="model.useTime"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
                :disabled="disableSubmit"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="备注" prop="remark">
              <a-textarea :rows="3" v-model="model.remark" :disabled="disableSubmit" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="图片预览" prop="1">
              <j-image-upload
                class="avatar-uploader"
                text="上传"
                v-model="model.images"
                :disabled="true"
              ></j-image-upload>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { addRole, editRole, duplicateCheck } from '@/api/api'
import { deleteAction, postAction, getAction, putAction } from '@/api/manage'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import taskModalAddComponent from './taskModalAddComponent'
export default {
  name: 'taskModalAddEdit', //任务对话框
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JselectDqDepart,
    taskModalAddComponent
  },
  data() {
    return {
      model2: {},
      title: '操作',
      visible: false,
      visible2: false,
      roleDisabled: false,
      disableSubmit: false,
      model: {},
      layout: {
        labelCol: { span: 3 },
        wrapperCol: { span: 14 }
      },
      confirmLoading: false,
      validatorRules: {
        // roleName: [
        //   { required: true, message: '请输入角色名称!' },
        //   // { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        // ],
        // roleCode: [
        //   { required: true, message: '请输入角色名称!' },
        //   // { min: 0, max: 64, message: '长度不超过 64 个字符', trigger: 'blur' },
        //   // { validator: this.validateRoleCode}
        // ],
        taskId: [{ required: true, message: '请输入任务编号!' }],
        constructionSite: [{ required: true, message: '请输入施工部位!' }],
        applicant: [{ required: true, message: '请输入申请人!' }],
        applicantUnit: [{ required: true, message: '请输入申请单位!' }],
        teamLeaderId: [{ required: true, message: '请输入班组长!' }],
        factoryId: [{ required: true, message: '请选择加工厂!' }],
        useTime: [{ required: true, message: '请选择使用时间!' }]
      },
      dataSource: [],
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
          title: '数量',
          align: 'center',
          dataIndex: 'componentNumber'
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
        // /rebarTaskChecklist/rebarTaskChecklist/list
        list: '',
        delete: '/rebarTaskChecklist/rebarTaskChecklist/delete',
        deleteBatch: '/rebarTaskChecklist/rebarTaskChecklist/deleteBatch',
        exportXlsUrl: '/rebarTaskChecklist/rebarTaskChecklist/exportXls',
        importExcelUrl: 'rebarTaskChecklist/rebarTaskChecklist/importExcel'
      },
      id: 0,
      isDetail: false,
      jiagongchang: [],
      banzuzhang: []
    }
  },
  created() {
    //备份model原始值

    this.modelDefault = JSON.parse(JSON.stringify(this.model))
    this.modelDefault.applicant = this.$store.state.user.info.realname
    this.getSelectData()
    console.log('我是新的表单')
  },
  methods: {
    getSelectData() {
      getAction('/rebarFactory/rebarFactory/list', {
        pageNo: 1,
        pageSize: 999
      }).then(res => {
        this.jiagongchang = res.result.records
      })
    },
    handlerChangeSelect(factoryId) {
      // this.model.teamLeaderId = ''
      getAction('/rebarPersonnel/rebarPersonnel/getLeadTeamByFactory', {
        pageNo: 1,
        pageSize: 999,
        factoryId
      }).then(res => {
        this.banzuzhang = res.result.records
      })
    },
    handleDeleteId(id) {
      this.dataSource = this.dataSource.filter(x => x.id !== id)
    },
    handlerEdit() {
      this.dataSource.find(x => x.id == this.id).componentNumber = this.model2.componentNumber
      this.visible2 = false
    },
    handleEdit222(id) {
      this.id = id
      this.visible2 = true
    },
    handlerOk() {},
    add(selectionRows) {
      let a = this.modelDefault
      a.images = selectionRows.map(x => x.image)
      this.edit(a)
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
    async getBootomData(record) {
      const res = await getAction('/rebarTaskChecklist/rebarTaskChecklist/queryByTaskId', { id: record.id })
      console.log(res)
      this.dataSource = res.result.rebarComponentTaskList || []
    },
    close() {
      this.$refs.form.clearValidate()
      this.dataSource = []
      this.isDetail = false
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.model.teamLeader = that.banzuzhang.find(x => x.teamLeaderId == that.model.teamLeaderId).teamLeaderName
          that.$emit('handlerOk1', that.model)
          that.visible = false
          this.$confirm({
            title: '任务启动成功，是否跳转到构件任务管理查看?',
            onOk() {
              that.$router.push({ path: '/rebar/taskManagement' })
            },
            onCancel() {}
          })
        }
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
