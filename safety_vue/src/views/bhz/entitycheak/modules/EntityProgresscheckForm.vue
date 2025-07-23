<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="单位工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unitProject">
              <a-input v-model="model.unitProject" placeholder="请输入单位工程" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="工程类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectType">
              <j-dict-select-tag type="list" v-model="model.projectType" dictCode="projectType" placeholder="请选择工程类型" />
<!--              <a-input v-model="model.projectType" placeholder="请输入工程类型" ></a-input>-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="设计总数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="designQuantity">
              <a-input v-model="model.designQuantity" placeholder="请输入设计总数量" ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24" >-->
<!--            <a-form-model-item label="开累完成总数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="finishedAmount">-->
<!--              <a-input v-model="model.finishedAmount" placeholder="请输入开累完成总数量" @input="input"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-model-item label="剩余数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remainingAmount">-->
<!--              <a-input v-model="model.remainingAmount" placeholder="请输入剩余数量" ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-model-item label="开累完成进度(%)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="schedule">-->
<!--              <a-input v-model="model.schedule" placeholder="请输入开累完成进度(%)" ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24" >
            <a-form-model-item label="数量单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quantityUnit">
              <a-input v-model="model.quantityUnit" placeholder="请输入数量单位" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="是否是关键节点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="iskey">
              <j-dict-select-tag type="list" v-model="model.iskey" dictCode="yn" placeholder="请选择是否是关键节点" />
<!--              <a-input v-model="model.iskey" placeholder="请输入是否是关键节点" ></a-input>-->
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24" >-->
<!--            <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">-->
<!--              <a-input-number v-model="model.status" placeholder="请输入状态" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">-->
<!--              <a-input v-model="model.remark" placeholder="请输入备注" ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24" >-->
<!--            <a-form-model-item label="唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uuid">-->
<!--              <a-input v-model="model.uuid" placeholder="请输入唯一码" ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="实体进度清单子表" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="entityCheckDetialTable.loading"
          :columns="entityCheckDetialTable.columns"
          :dataSource="entityCheckDetialTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'

  export default {
    name: 'EntityProgresscheckForm',
    mixins: [JEditableTableModelMixin],
    components: {
      JselectDqDepart
    },
    data() {
      return {
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
        model:{
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
           uuid: [
              { required: true, message: '请输入唯一码!'},
           ],
        },
        refKeys: ['entityCheckDetial', ],
        tableKeys:['entityCheckDetial', ],
        activeKey: 'entityCheckDetial',
        // 实体进度清单子表
        entityCheckDetialTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '计划时间(年-月)',
              key: 'time',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '计划数量',
              key: 'planAmount',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '完成数量',
              key: 'plannedAmount',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            // {
            //   title: '唯一码',
            //   key: 'uuid',
            //   type: FormTypes.input,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            //   validateRules: [{ required: true, message: '${title}不能为空' }],
            // },
            // {
            //   title: '计划完成进度(%)',
            //   key: 'planProgress',
            //   type: FormTypes.input,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
            // {
            //   title: '备注',
            //   key: 'remark',
            //   type: FormTypes.input,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
          ]
        },
        url: {
          add: "/entityprogresscheck/entityProgresscheck/add",
          edit: "/entityprogresscheck/entityProgresscheck/edit",
          queryById: "/entityprogresscheck/entityProgresscheck/queryById",
          entityCheckDetial: {
            list: '/entityprogresscheck/entityProgresscheck/queryEntityCheckDetialByMainId'
          },
        }
      }
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
    },
    methods: {
      addBefore(){
        this.entityCheckDetialTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
        })
        // 加载子表数据
        if (this.model.uuid) {
          let params = { id: this.model.uuid }
          this.requestSubTableData(this.url.entityCheckDetial.list, params, this.entityCheckDetialTable)
        }
      },
      //校验所有一对一子表表单
      validateSubForm(allValues){
          return new Promise((resolve,reject)=>{
            Promise.all([
            ]).then(() => {
              resolve(allValues)
            }).catch(e => {
              if (e.error === VALIDATE_NO_PASSED) {
                // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
                this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
              } else {
                console.error(e)
              }
            })
          })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          entityCheckDetialList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },

    }
  }
</script>

<style scoped>
</style>