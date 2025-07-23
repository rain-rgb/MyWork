<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-item label="配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['code']" placeholder="不填可默认生成理论配合比编号(手填请避免重复)"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="拌合机编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请选择设备名称" v-decorator="['bhjno']" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="唯一ID" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['uuid']" placeholder="请输入唯一ID" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->


<!--                    <a-col :span="12" >-->
<!--                      <a-form-item label="配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                        <a-input v-decorator="['code']" placeholder="可默认生成理论配合比编号(手填请避免重复)" ></a-input>-->
<!--                      </a-form-item>-->
<!--                    </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="砼标记" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['tag']" placeholder="请输入砼标记" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="混凝土类别" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['variety']" placeholder="请输入混凝土类别" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-row>
            <a-col :span="12">
              <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['betlevel']" :trigger-change="true" dictCode="betlev"
                                   placeholder="请选择强度等级"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="抗渗等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['filters']" placeholder="请输入抗渗等级"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="抗冻等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['freeze']" placeholder="请输入抗冻等级"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="抗折等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['bend']" placeholder="请输入抗折等级"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <!--            <a-col :span="24" >-->
            <!--              <a-form-item label="施工季节" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['season']" placeholder="请输入施工季节" ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="坍落度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['lands']" :trigger-change="true" dictCode="lands"
                                   placeholder="请选择坍落度"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="搅拌时长" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['mixlast']" placeholder="默认不写会设为默认值120" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <!--            <a-col :span="12" >-->
            <!--              <a-form-item label="设计比例" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['scale']" placeholder="请输入设计比例" ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--            <a-col :span="12" >-->
            <!--              <a-form-item label="骨料最大粒径" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input v-decorator="['bonesz']" placeholder="请输入骨料最大粒径" ></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->

            <a-col :span="12">
              <a-form-item label="水胶比" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['waterbindratio']" placeholder="请输入水胶比"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="每方重量kg" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['onevolume']" placeholder="请输入每方重量kg" style="width: 100%"/>
              </a-form-item>
            </a-col>

          </a-row>

          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="水泥品种" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['cementtype']" placeholder="请输入水泥品种" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="status" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['status']" placeholder="请输入status" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="建立时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['createdate']" placeholder="请输入建立时间" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->

          <!--          <a-col :span="24" >-->
          <!--            <a-form-item label="砂率" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['sandratio']" placeholder="请输入砂率" ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->

        </a-row>
      </a-form>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="砼拌合站理论配合比子表" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bhzRecipedetailTable.loading"
          :columns="bhzRecipedetailTable.columns"
          :dataSource="bhzRecipedetailTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"
          @valueChange="handleValueChange"
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
  name: 'BhzRecipeForm',
  mixins: [JEditableTableMixin],
  components: {
    JFormContainer,
    JEditableTable
  },
  data() {
    return {
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
      refKeys: ['bhzRecipedetail',],
      tableKeys: ['bhzRecipedetail',],
      activeKey: 'bhzRecipedetail',
      // 砼拌合站理论配合比子表
      bhzRecipedetailTable: {
        loading: false,
        dataSource: [],
        columns: [
          // {
          //   title: '唯一区分的id',
          //   key: 'origid',
          //   type: FormTypes.input,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          //   validateRules: [{ required: true, message: '${title}不能为空' }],
          // },
          // {
          //   title: '配合比id',
          //   key: 'recipeid',
          //   type: FormTypes.input,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },
          // {
          //   title: '材料id',
          //   key: 'materialid',
          //   type: FormTypes.input,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },

          {
            title: '材料类型',
            key: 'materialTypes',
            dictCode:'material_types',
            width: '300px',
            type: FormTypes.select,
            //slotName: 'materialTypes',
            options: [],
            placeholder: '请选择${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          // {
          //   title: '材料类型',
          //   key: 'materialTypes',
          //   type: FormTypes.slot,
          //   slotName: 'action',
          //   width: '300px',
          // },
          {
            title: '材料名称',
            key: 'materialname',
            dictCode:'',
            type: FormTypes.select,
            width: '300px',
            //slotName: 'materialname',
            options: [],
            placeholder: '请输入${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          {
            title: '用量',
            key: 'amount',
            type: FormTypes.inputNumber,
            // width:"200px",
            placeholder: '请输入${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          // {
          //   title: 'status',
          //   key: 'status',
          //   type: FormTypes.inputNumber,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },
        ]
      },
      url: {
        add: '/bhzrecipe/bhzRecipe/add',
        edit: '/bhzrecipe/bhzRecipe/edit',
        queryById: '/bhzrecipe/bhzRecipe/queryById',
        bhzRecipedetail: {
          list: '/bhzrecipe/bhzRecipe/queryBhzRecipedetailByMainId'
        },
        list1: '/sys/dictItem/cailiaonamelist'
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
    /** 当选项被改变时，联动其他组件 */
    handleValueChange(event) {
      const { type, row, column, value, target } = event
      console.log(event)
      if (type === FormTypes.select) {
        // 第一列
        if (column.key === 'materialTypes') {
          this.mtype=value;//拿到选择的类型并赋值
          console.log(this.mtype)
          this.cailiaoname();
        }
      }
    },
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
      this.bhzRecipedetailTable.dataSource = []
    },
    getAllTable() {
      let values = this.tableKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      let fieldval = pick(this.model,  'betlevel', 'filters', 'freeze', 'bend',  'lands', 'mixlast', 'bhjno',  'waterbindratio', 'onevolume')
      this.$nextTick(() => {
        this.form.setFieldsValue(fieldval)
      })
      // 加载子表数据
      if (this.model.uuid) {
        let params = { id: this.model.uuid }
        this.requestSubTableData(this.url.bhzRecipedetail.list, params, this.bhzRecipedetailTable)
      }
    },
    /** 整理成formData */
    classifyIntoFormData(allValues) {
      let main = Object.assign(this.model, allValues.formValue)
      return {
        ...main, // 展开
        bhzRecipedetailList: allValues.tablesValue[0].values,
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
        sbtypes: '0'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    cailiaoname: function () {//材料名称下拉选择
      console.log(this.mtype)
      let params = { itemValue: this.mtype }
      this.bhzRecipedetailTable.columns[1].options = []
      getAction(this.url.list1, params)
        .then(res => {
         // console.log(res, 'MMMMMMMMMMMMMMMMMMMMMMMMMM')
          if (res.success) {
            let data = res.result
            console.log(this.bhzRecipedetailTable.columns[1].options)
            for (let i = 0; i < data.length; i++) {
              this.bhzRecipedetailTable.columns[1].options.push({
                text: data[i].itemValue,
                title: data[i].itemValue,
                value: data[i].itemValue
              })
            }
          }
        })
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row,  'betlevel', 'filters', 'freeze', 'bend',  'lands', 'mixlast',  'bhjno',  'waterbindratio', 'onevolume'))
    },

  }
}
</script>

<style scoped>
</style>