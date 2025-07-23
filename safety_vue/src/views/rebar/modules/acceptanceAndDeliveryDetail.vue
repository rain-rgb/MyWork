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
            <a-form-model-item label="任务编号" prop="taskId">
              <a-input v-model="model.taskId" :disabled="roleDisabled || disableSubmit" placeholder="请输入任务编号" />
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="12">
            <a-form-model-item label="施工部位" prop="constructionSite1">
              <JselectDqDepart v-model="model.constructionSite" :disabled="disableSubmit" />
              <JSelectDqProjName v-model="model.orgCode"  />
            </a-form-model-item>
          </a-col> -->
          <a-col :span="12">
            <a-form-model-item label="使用时间" prop="useTime" width="100%">
              <j-date
                placeholder="请选择使用时间"
                v-model="model.useTime"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
                :disabled="disableSubmit"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
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
            <a-form-model-item label="加工厂" prop="factoryName">
              <a-input v-model="model.factoryName" :disabled="disableSubmit" placeholder="请输入加工厂" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="班组长" prop="teamLeader">
              <a-input v-model="model.teamLeader" :disabled="disableSubmit" placeholder="请输入班组长" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="备注" prop="remark">
              <a-textarea :rows="3" v-model="model.remark" :disabled="disableSubmit" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-spin>
    <span style="font-size: 16px;margin-bottom: 10px;">构件列表：</span>
    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus" :disabled="disableSubmit">新增</a-button>
    </div> -->
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
        <!-- <a @click="handleEdit222(record.id)" :disabled="disableSubmit">编辑</a> -->

        <a-divider type="vertical" />
        <a-dropdown>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDeleteId(record.id)">
            <a :disabled="disableSubmit">删除</a>
          </a-popconfirm>
        </a-dropdown>
      </span>
    </a-table>
    <taskModalAddComponent
      ref="modalForm"
      @handlerOk="handlerOk"
      :dataSourceParaent="dataSource"
      :disableSubmitButton="disableSubmit"
    />
    <a-modal
      title="编辑"
      :width="600"
      :visible="visible2"
      @ok="handlerEdit"
      @cancel="visible2 = false"
      cancelText="关闭"
    >
      <a-form-model ref="form" v-bind="layout" :model="model2" :rules="validatorRules" :labelCol="{ span: 6 }">
        <a-form-model-item label="数量" prop="componentNumber">
          <a-input v-model="model2.componentNumber" placeholder="请输入数量" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>

    <!-- <template slot="footer" v-if="isDetail">

      <a-button type="primary" :loading="loading" @click="handleOk(4)">
        验收
      </a-button>
      <a-button  type="primary" :loading="loading" @click="handleOk(5)">
        出库
      </a-button>
      <a-button @click="printXls" v-has="'shigongphb:print'" type="primary" icon="printer">生成出库码</a-button>
    </template> -->
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
import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
export default {
  name: 'acceptanceAndDeliveryDetail', //任务对话框
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JselectDqDepart,
    taskModalAddComponent,
    JSelectDqProjName
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
        // taskId: [{ required: true, message: '请输入任务编号!' }],
        // constructionSite: [{ required: true, message: '请输入施工部位!' }],
        // applicant: [{ required: true, message: '请输入申请人!' }],
        // applicantUnit: [{ required: true, message: '请输入申请单位!' }],
        // teamLeader: [{ required: true, message: '请输入班组长!' }],
        // factoryName: [{ required: true, message: '请输入加工厂!' }],
        // useTime: [{ required: true, message: '请选择使用时间!' }]
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
        }

        // {
        //   title: '操作',
        //   dataIndex: 'action',
        //   align: "center",
        //   fixed: "right",
        //   width: 147,
        //   scopedSlots: { customRender: 'action' }
        // }
      ],
      url: {
        // /rebarTaskChecklist/rebarTaskChecklist/list
        list: '',
        delete: '/rebarTaskChecklist/rebarTaskChecklist/delete',
        deleteBatch: '/rebarTaskChecklist/rebarTaskChecklist/deleteBatch',
        exportXlsUrl: '/rebarTaskChecklist/rebarTaskChecklist/exportXls',
        importExcelUrl: 'rebarTaskChecklist/rebarTaskChecklist/importExcel',
        printUrl: 'jmreport/view/585690650923728896'
      },
      id: 0,
      isDetail: false
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
    console.log('我是新的表单')
  },
  methods: {
    printXls() {
      //打印功能需要先去报表设计页面设计打印格式
      let url = `${window._CONFIG['domianURL']}/${this.url.printUrl}?id=${this.model.id}&token=${this.token}`
      window.open(url)
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
    handlerOk(selectionRows) {
      selectionRows.forEach(item => {
        if (this.dataSource.findIndex(x => x.id == item.id) == -1) {
          item.componentNumber = 1
          this.dataSource.push(item)
        }
      })
    },
    add() {
      this.edit(this.modelDefault)
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
      const res = await getAction('/rebarTaskChecklist/rebarTaskChecklist/queryByTaskId', {
        id: record.id,
        orgCodes: record.orgCodes
      })
      console.log(res)
      this.model = res.result
      this.dataSource = res.result.rebarComponentTaskList || []
    },
    close() {
      this.$refs.form.clearValidate()
      this.dataSource = []
      this.isDetail = false
      this.$emit('close')
      this.visible = false
    },
    handleOk(status = 0) {
      const that = this
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          this.model.outboundPersonnel = this.$store.state.user.info.realname
          this.model.outboundTime = this.getNowDate()
          this.model.status = 5
          putAction('/rebarTaskChecklist/rebarTaskChecklist/edit', this.model).then(res => {
            this.$emit('ok')
            this.visible = false
          })
        } else {
          return false
        }
      })
    },
    //js获取当前时间
    getNowDate() {
      var myDate = new Date()
      var year = myDate.getFullYear() //获取当前年
      var mon = myDate.getMonth() + 1 //获取当前月
      if (mon < 10) {
        mon = '0' + mon
      }
      var date = myDate.getDate() //获取当前日
      if (date < 10) {
        date = '0' + date
      }
      var hours = myDate.getHours() //获取当前小时
      if (hours < 10) {
        hours = '0' + hours
      }
      var minutes = myDate.getMinutes() //获取当前分钟
      if (minutes < 10) {
        minutes = '0' + minutes
      }
      var seconds = myDate.getSeconds() //获取当前秒
      if (seconds < 10) {
        seconds = '0' + seconds
      }
      var now = year + '-' + mon + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds
      return now
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
