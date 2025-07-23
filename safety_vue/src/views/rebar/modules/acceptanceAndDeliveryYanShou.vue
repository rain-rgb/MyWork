<template>
  <a-modal :title="title" :width="1200" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk(0)"
    @cancel="handleCancel" cancelText="关闭" wrapClassName="ant-modal-cust-warp"
    style="top:5%;height: 85%;overflow-y: hidden">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" v-bind="layout" :model="model" :rules="validatorRules" :labelCol="{ span: 6 }">
        <!-- required -->
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="验收人员" prop="acceptancePersonnel">
              <a-input v-model="model.acceptancePersonnel" :disabled="disableSubmit"
                placeholder="请输入验收人员" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="验收说明" prop="acceptanceInstructions">
              <a-textarea :rows="3" v-model="model.acceptanceInstructions" :disabled="disableSubmit"
                placeholder="请输入验收说明" />
            </a-form-model-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="12">
            <a-form-model-item label="上传图片" prop="1">
              <j-image-upload class="avatar-uploader" text="上传" v-model="model.acceptanceImage" :disabled="disableSubmit"></j-image-upload>
            </a-form-model-item>
          </a-col>

        </a-row>

      </a-form-model>
    </a-spin>
    <template #footer>
      <a-button  key="back" @click="handleCancel">
        关闭
      </a-button>
      <a-button v-if="!disableSubmit" key="submit" type="primary" :loading="confirmLoading" @click="handleOk(0)">
        确定
      </a-button>
    </template>
  </a-modal>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { addRole, editRole, duplicateCheck } from '@/api/api'
import { deleteAction, postAction, getAction, putAction } from '@/api/manage'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import taskModalAddComponent from './taskModalAddComponent';
import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
export default {
  name: "taskModalAddEdit",//任务对话框
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JselectDqDepart,
    taskModalAddComponent,
    JSelectDqProjName
  },
  data() {
    return {
      model2: {},
      title: "操作",
      visible: false,
      visible2: false,
      roleDisabled: false,
      disableSubmit: false,
      model: {},
      layout: {
        labelCol: { span: 3 },
        wrapperCol: { span: 14 },
      },
      confirmLoading: false,
      validatorRules: {
        acceptancePersonnel: [
          { required: true, message: '请输入验收人员!' },
        ],
        acceptanceInstructions: [
          { required: true, message: '请输入验收说明!' },
          // { min: 0, max: 64, message: '长度不超过 64 个字符', trigger: 'blur' },
          // { validator: this.validateRoleCode}
        ],
        taskId: [{ required: true, message: '请输入任务编号!' },],
        constructionSite: [{ required: true, message: '请输入施工部位!' },],
        applicant: [{ required: true, message: '请输入申请人!' },],
        applicantUnit: [{ required: true, message: '请输入申请单位!' },],
        teamLeader: [{ required: true, message: '请输入班组长!' },],
        processors: [{ required: true, message: '请输入加工负责人!' },],
        useTime: [{ required: true, message: '请选择使用时间!' },],

      },
      dataSource: [],
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
        // /rebarTaskChecklist/rebarTaskChecklist/list
        list: "",
        delete: "/rebarTaskChecklist/rebarTaskChecklist/delete",
        deleteBatch: "/rebarTaskChecklist/rebarTaskChecklist/deleteBatch",
        exportXlsUrl: "/rebarTaskChecklist/rebarTaskChecklist/exportXls",
        importExcelUrl: "rebarTaskChecklist/rebarTaskChecklist/importExcel",
        printUrl: 'jmreport/view/585690650923728896',
      },
      id: 0,
      isDetail: false,

    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
    console.log('我是新的表单');
  },
  methods: {
    printXls() {//打印功能需要先去报表设计页面设计打印格式
      let url = `${window._CONFIG['domianURL']}/${this.url.printUrl}?id=${this.model.id}&token=${this.token}`;
      window.open(url);
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
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.model.acceptancePersonnel=this.$store.state.user.info.realname
      this.visible = true;
      //编辑页面禁止修改角色编码
      if (this.model.id) {
        this.roleDisabled = true;
      } else {
        this.roleDisabled = false;
      }
    },
    async getBootomData(record) {
      const res = await getAction('/rebarTaskChecklist/rebarTaskChecklist/queryByTaskId', { id: record.id })
      console.log(res);
      this.dataSource = res.result.rebarComponentTaskList || []
    },
    close() {
      this.$refs.form.clearValidate();
      this.dataSource = []
      this.isDetail = false
      this.$emit('close');
      this.visible = false;
    },
    handleOk(status = 0) {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          this.model.acceptanceTime = this.getNowDate()
          this.model.status = 4
          putAction('/rebarTaskChecklist/rebarTaskChecklist/edit', this.model).then(res => {
            this.$emit('ok')
            this.visible = false
          })
        } else {
          return false;
        }
      })
    },
    //js获取当前时间
    getNowDate() {
      var myDate = new Date;
      var year = myDate.getFullYear(); //获取当前年
      var mon = myDate.getMonth() + 1; //获取当前月
      if (mon < 10) {
        mon = "0" + mon;
      }
      var date = myDate.getDate(); //获取当前日
      if (date < 10) {
        date = "0" + date;
      }
      var hours = myDate.getHours(); //获取当前小时
      if (hours < 10) {
        hours = "0" + hours;
      }
      var minutes = myDate.getMinutes(); //获取当前分钟
      if (minutes < 10) {
        minutes = "0" + minutes;
      }
      var seconds = myDate.getSeconds(); //获取当前秒
      if (seconds < 10) {
        seconds = "0" + seconds;
      }
      var now = year + "-" + mon + "-" + date + " " + hours + ":" + minutes + ":" + seconds;
      return now;
    },
    handleCancel() {
      this.close()
    },
    validateRoleCode(rule, value, callback) {
      if (/[\u4E00-\u9FA5]/g.test(value)) {
        callback("角色编码不可输入汉字!");
      } else {
        let params = {
          tableName: "sys_role",
          fieldName: "role_code",
          fieldVal: value,
          dataId: this.model.id,
        };
        duplicateCheck(params).then((res) => {
          if (res.success) {
            callback();
          } else {
            callback(res.message);
          }
        });
      }
    },

  }
}
</script>

<style scoped></style>