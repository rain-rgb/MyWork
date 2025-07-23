<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    style="top:5%;height: 85%;overflow-y: hidden"
  >
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" v-bind="layout" :model="model" :rules="validatorRules">
        <!-- required -->
        <a-form-model-item label="构件编码" prop="componentId">
          <a-input v-model="model.componentId" :disabled="roleDisabled || disableSubmit" placeholder="请输入构件编码" />
        </a-form-model-item>
        <!-- <a-form-model-item label="所属分部分项" prop="a">
          <JselectDqDepart v-model="model.orgCode" :disabled="disableSubmit" />
          <JSelectDqProjName v-model="model.orgCodes" ::multi="false"/>
        </a-form-model-item> -->
        <a-form-model-item label="构件名称" prop="componentName">
          <a-input v-model="model.componentName" :disabled="disableSubmit" placeholder="请输入构件名称" />
        </a-form-model-item>
        <a-form-model-item label="规格型号" prop="componentModel">
          <a-input v-model="model.componentModel" :disabled="disableSubmit" placeholder="请输入规格型号" />
        </a-form-model-item>
        <a-form-model-item label="构件类型" prop="componentType">
          <j-dict-select-tag
            placeholder="请选择构件类型"
            :disabled="disableSubmit"
            v-model="model.componentType"
            dictCode="component_type"
          ></j-dict-select-tag>
        </a-form-model-item>
        <a-form-model-item label="重量" prop="weight">
          <a-input v-model="model.weight" :disabled="disableSubmit" placeholder="请输入重量" />
        </a-form-model-item>
        <a-form-model-item label="单位" prop="unit">
          <!-- <a-input v-model="model.unit" :disabled="disableSubmit" placeholder="请输入单位" /> -->
          <j-dict-select-tag
            placeholder="请选择单位"
            :disabled="disableSubmit"
            v-model="model.unit"
            dictCode="WZCaiLiaoJiLiangDanWei"
          ></j-dict-select-tag>
        </a-form-model-item>
        <a-form-model-item label="备注" prop="remark">
          <a-textarea :rows="5" v-model="model.remark" :disabled="disableSubmit" placeholder="请输入备注" />
        </a-form-model-item>
        <a-form-model-item label="上传图片" prop="1">
          <j-image-upload class="avatar-uploader" text="上传" v-model="model.image"></j-image-upload>
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
import { addRole, editRole, duplicateCheck } from '@/api/api'
import { deleteAction, postAction, getAction, putAction } from '@/api/manage'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
export default {
  name: 'IngredientManagementAddEdit', //构件对框框
  components: {
    JselectDqDepart,
    JSelectDqProjName
  },
  data() {
    return {
      title: '操作',
      visible: false,
      roleDisabled: false,
      disableSubmit: false,
      model: {
        orgCodes: ''
      },
      layout: {
        labelCol: { span: 3 },
        wrapperCol: { span: 14 }
      },
      confirmLoading: false,
      validatorRules: {
        roleName: [
          // { required: true, message: '请输入角色名称!' },
          // { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
          // { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }\
          //  { required: true, message: '请输入角色名称!' },
          // { min: 0, max: 64, message: '长度不超过 64 个字符', trigger: 'blur' },
          // { validator: this.validateRoleCode}
        ],

        componentId: [{ required: true, message: '请输入构件编码!' }],
        orgCode: [{ required: true, message: '请选择所属部门!' }],
        componentName: [{ required: true, message: '请输入构件名称!' }],
        componentModel: [{ required: true, message: '请输入规格型号!' }],
        componentType: [{ required: true, message: '请选择构件类型!' }],
        weight: [{ required: true, message: '请输入重量!' }],
        unit: [{ required: true, message: '请输入单位!' }]
      }
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
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
    close() {
      this.$refs.form.clearValidate()
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          // that.confirmLoading = true;
          let obj
          if (!this.model.id) {
            // obj=addRole(this.model);
            console.log('我是新增', this.model)
            obj = postAction('/rebarComponent/rebarComponent/add', this.model)
          } else {
            // obj=editRole(this.model);
            obj = putAction('/rebarComponent/rebarComponent/edit', this.model)
          }
          obj
            .then(res => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
                that.close()
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
              //
            })
        } else {
          return false
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
