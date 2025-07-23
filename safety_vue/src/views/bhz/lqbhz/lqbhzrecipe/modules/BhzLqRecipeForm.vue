<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="混合料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['hhllx']" :trigger-change="true" dictCode="hhllx"
                                   placeholder="请选择混合料类型"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['gcmc']" placeholder="请输入工程名称"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="配比名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['lilunname']" :trigger-change="true" dictCode="lilunname"
                                   placeholder="请选择配比名称"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['llbuwei']" :trigger-change="true" dictCode="llbuwei"
                                   placeholder="请选择施工部位"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag placeholder="请选择设备名称" v-decorator="['shebeibianhao']" :dictOptions="dictOption">
                </j-search-select-tag>
                {{ selectValue }}
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="理论油石比" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['llysb']" placeholder="请输入理论油石比"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-col :span="12">
            <a-form-item label="配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['phbid']" placeholder="请输入配合比编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="当前使用" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-switch
                default-checked
                :auto-focus="false"
                :checked="checked"
                checked-children="是"
                un-checked-children="否"
                :loading="loading"
                @change="onChange" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="沥青拌合站理论配合比子表" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bhzLqPhbZibiaoTable.loading"
          :columns="bhzLqPhbZibiaoTable.columns"
          :dataSource="bhzLqPhbZibiaoTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"
          @confirm="handleConfirmDelete">
        </j-editable-table>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

import pick from 'lodash.pick'
import { getAction, httpAction } from '@/api/manage'
import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import { cloneObject, validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import { ajaxGetDictItems, usershebeiList } from '@api/api'
import Vue from 'vue'
import JEditableTable from '@comp/jeecg/JEditableTable'

export default {
  name: 'BhzLqRecipeForm',
  mixins: [JEditableTableMixin],
  components: {
    JFormContainer,
    JEditableTable
  },
  data() {
    return {
      checked:true,
      loading: false,
      productsList: [],
      material_types: '',
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      labelCol2: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol2: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      mtype: '',
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 0,
      refKeys: ['bhzLqPhbZibiao',],
      tableKeys: ['bhzLqPhbZibiao',],
      activeKey: 'bhzLqPhbZibiao',
      // 砼拌合站理论配合比子表
      bhzLqPhbZibiaoTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '材料名称',
            key: 'cailiaono',
            type: FormTypes.input,
            width: '300px',
            placeholder: '请输入${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          {
            title: '理论用量(%)',
            key: 'tianjiaji',
            type: FormTypes.inputNumber,
            // width:"200px",
            placeholder: '请输入${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
        ]
      },
      url: {
        add: "/lqbhzrecipe/bhzLqRecipe/add",
        edit: "/lqbhzrecipe/bhzLqRecipe/edit",
        queryById: '/bhzrecipe/bhzRecipe/queryById',
        bhzLqPhbZibiao: {
          list: '/lqbhzrecipe/bhzLqPhbZibiao/list1'
        },
      }
    }
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
      required: false
    },
    //表单模式：false流程表单 true普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    }
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
    this.shebeiList()
  },
  methods: {
    handleConfirmDelete() {
      this.removeSelectedRows()
    },
    /** 删除被选中的行 */
    removeSelectedRows() {
      console.log(this.selectedRowIds)
      this.removeRows(this.selectedRowIds)
      this.selectedRowIds = []
    },
    /** 删除一行或多行 */
    removeRows(id) {
      let ids = id
      if (!(id instanceof Array)) {
        if (typeof id === 'string') {
          ids = [id]
        } else {
          throw  `JEditableTable.removeRows() 函数需要的参数可以是string或Array类型，但提供的却是${typeof id}`
        }
      }

      let rows = cloneObject(this.rows)
      ids.forEach(removeId => {
        removeId = this.getCleanId(removeId)
        // 找到每个id对应的真实index并删除
        const findAndDelete = (arr) => {
          for (let i = 0; i < arr.length; i++) {
            let currentId = this.getCleanId(arr[i].id)
            if (currentId === removeId) {
              arr.splice(i, 1)
              return true
            }
          }
        }
        // 找到rows对应的index，并删除
        if (findAndDelete(rows)) {
          // 找到values对应的index，并删除
          findAndDelete(this.inputValues)
          // 将caseId去除
          let id = this.getCleanId(removeId)
          this.deleteIds.push(id)
        }
      })
      this.rows = rows
      this.$emit('deleted', this.getDeleteIds(), this)
      this.$nextTick(() => {
        // 更新formValues
        this.updateFormValues()
      })
      return true
    },

    addBefore() {
      this.form.resetFields()
      this.bhzLqPhbZibiaoTable.dataSource = []
    },
    getAllTable() {
      let values = this.tableKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      let fieldval = pick(this.model,'llysb','shebeibianhao','lilunname','llbuwei','hhllx','gcmc','phbid')
      this.$nextTick(() => {
        if (this.checked===true){
          this.model.llmoren = 1
        }else {
          this.model.llmoren = 0
        }
        this.form.setFieldsValue(fieldval)
      })
      // 加载子表数据
      if (this.model.zhuziid) {
        let params = { zhuziid: this.model.zhuziid }
        this.requestSubTableData(this.url.bhzLqPhbZibiao.list, params, this.bhzLqPhbZibiaoTable)
      }
      this.checked = this.model.llmoren !== 0;
      //console.log("this.checked",this.checked)
    },
    /** 整理成formData */
    classifyIntoFormData(allValues) {
      let main = Object.assign(this.model, allValues.formValue)
      return {
        ...main, // 展开
        bhzLqPhbZibiaoList: allValues.tablesValue[0].values,
      }
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId }
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result)
          }
        })
      }
    },
    validateError(msg) {
      this.$message.error(msg)
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '1'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    onChange(checked){
      this.checked = checked
      if (this.checked===true){
        this.model.llmoren = 1
      }else {
        this.model.llmoren = 0
      }
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row,   'llysb','shebeibianhao','lilunname','llbuwei','hhllx','gcmc','phbid'))
    },

  }
}
</script>

<style scoped>
</style>