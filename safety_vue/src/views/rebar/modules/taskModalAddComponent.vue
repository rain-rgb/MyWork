<template>
  <a-modal :title="title" :width="1200" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
    @cancel="handleCancel" cancelText="关闭" wrapClassName="ant-modal-cust-warp"
    style="top:5%;height: 85%;overflow-y: hidden">

    <a-table ref="table" size="middle" :scroll="{ x: true }" bordered rowKey="id" :columns="columns"
      :dataSource="dataSource" :pagination="ipagination" :loading="loading" class="j-table-force-nowrap"
      :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" @change="handleTableChange">

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
export default {
  name: "taskModalAddComponent",//任务对话框
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JselectDqDepart,
  },
  props: ['dataSourceParaent', 'disableSubmitButton', 'currentDeptId'],
  data() {
    return {
      title: "操作",
      visible: false,
      roleDisabled: false,
      disableSubmit: false,
      model: {},
      layout: {
        labelCol: { span: 3 },
        wrapperCol: { span: 14 },
      },
      confirmLoading: false,
      validatorRules: {
        roleName: [
          { required: true, message: '请输入角色名称!' },
          // { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '请输入角色名称!' },
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
          dataIndex: 'componentName',
        },

        {
          title: '规格型号',
          align: "center",
          dataIndex: 'componentModel'
        },
        {
          title: '单位',
          align: "center",
          dataIndex: 'unit_dictText'
        },

        {
          title: '备注',
          align: "center",
          dataIndex: 'remark'
        },


      ],
      url: {
        list: "/rebarComponent/rebarComponent/list",
        delete: "/rebarTaskChecklist/rebarTaskChecklist/delete",
        deleteBatch: "/rebarTaskChecklist/rebarTaskChecklist/deleteBatch",
        exportXlsUrl: "/rebarTaskChecklist/rebarTaskChecklist/exportXls",
        importExcelUrl: "rebarTaskChecklist/rebarTaskChecklist/importExcel",

      },
      queryParam: {}
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    add(currentDeptId) {
      this.edit(this.modelDefault);
      this.queryParam.sys_depart_orgcodes = currentDeptId
      this.loadData()
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.visible = true;
      //编辑页面禁止修改角色编码
      if (this.model.id) {
        this.roleDisabled = true;
      } else {
        this.roleDisabled = false;
      }

    },
    close() {
      // this.$refs.form.clearValidate();
      // this.$emit('close');
      this.visible = false;
      // this.dataSource=[]
    },
    handleOk() {
      this.$emit('handlerOk', this.selectionRows)
      this.visible = false
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