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
        <a-form-model-item label="材料类型" prop="materialId">
          <a-select
            placeholder="请选择规格型号"
            v-model="model.nodeType"
            show-search
            allowClear
            option-filter-prop="children"
            @change="handlerChangeSelect(1)"
          >
            <a-select-option :value="undefined">请选择</a-select-option>
            <a-select-option v-for="item in dictOptions" :key="`${item.value}`">
              {{ item.title }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="规格型号" prop="materialId">
          <a-select
            placeholder="请选择规格型号"
            v-model="model.materialType"
            show-search
            allowClear
            option-filter-prop="children"
            @change="handlerChangeSelect2(1)"
          >
            <a-select-option :value="undefined">请选择</a-select-option>
            <a-select-option v-for="item in dictOptions2" :key="`${item.id}`" :value="item.guigexinghao">
              {{ item.guigexinghao }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="材料名称" prop="materialId">
          <!-- :disabled="disableSubmit" -->
          <a-select
            placeholder="请选择材料名称"
            v-model="model.materialId"
            allowClear
            show-search
            option-filter-prop="children"
          >
            <a-select-option :value="undefined">请选择</a-select-option>
            <a-select-option v-for="item in dictOptions3" :key="`${item.id}`" :value="item.cailiaono">
              {{ item.cailiaoname }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="数量" prop="materialNumber">
          <a-input v-model="model.materialNumber" placeholder="请输入数量" />
        </a-form-model-item>
        <!-- <a-form-model-item label="备注" prop="remark">
          <a-textarea :rows="5" v-model="model.remark" :disabled="disableSubmit" placeholder="请输入备注" />
        </a-form-model-item> -->
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
import { addRole, editRole, duplicateCheck } from '@/api/api'
import { deleteAction, postAction, getAction, putAction } from '@/api/manage'
export default {
  name: 'marticalModalAddEdit', //材料对话框
  data() {
    return {
      title: '操作',
      visible: false,
      roleDisabled: false,
      disableSubmit: false,
      model: {
        pageNo: 1,
        pageSize: 999
      },
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
      getPopupContainer: {
        type: Function,
        default: node => node.parentNode
      },
      dictOptions: [],
      dictOptions2: [],
      dictOptions3: [],
      modelCopy: {} //传进来的原始数据
    }
  },
  props: ['currentRoleId'],
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
    this.initData()
  },
  methods: {
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.modelCopy = Object.assign({}, record)
      this.handlerChangeSelect()
      this.handlerChangeSelect2()
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
          if (this.title == '新增') {
            // obj=addRole(this.model);
            console.log('我是新增触发表单验证', this.model)
            this.model.componentId = this.currentRoleId
            this.model.materialName=this.dictOptions3.find(x=>x.cailiaono==this.model.materialId).cailiaoname
            obj = postAction('/rebarComponent/rebarComponent/addMaterialToComponentId', this.model)
          } else {
            // obj=editRole(this.model);
            this.model.componentId = this.currentRoleId
            obj = putAction('/rebarComponent/rebarComponent/editByComponentIdAndMaterial', this.model)
          }
          obj
            .then(res => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
              that.close()
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
    },
    async initData() {
      console.log('我是初始化')
      const res = await getAction('/sys/dict/getDictItems/nodetype')
      console.log(res)
      this.dictOptions = res.result
      // const res = await getAction('/rebarComponent/rebarComponent/queryMaterialByNodeType', { nodeType: 12 })
      // this.dictOptions = res.result.map(item => ({ ...item, titleText: item.cailiaoname + ':' + item.guigexinghao }))
    },
    async handlerChangeSelect(type = 0) {
      this.dictOptions2 = []
      this.dictOptions3 = []
      if (type == 1) delete this.model.guigexinghao
      if (type == 1) delete this.model.materialId
      const res = await getAction('/wzcailiaonamedictman/wzcailiaonamedictMan/rebarList', {
        nodetypeCP: this.model.nodeType,
        isdel: 0,
        pageNo: 1,
        pageSize: 9999
      })
      this.dictOptions2 = res.result.records || []
    },
    async handlerChangeSelect2(type = 0) {
      this.dictOptions3 = []
      if (type == 1) delete this.model.materialId
      const res = await getAction('/wzcailiaonamedictman/wzcailiaonamedictMan/list', {
        nodeType: this.model.nodeType,
        guigexinghao: this.model.guigexinghao
      })
      this.dictOptions3 = res.result.records || []
    }
  }
}
</script>

<style scoped></style>
