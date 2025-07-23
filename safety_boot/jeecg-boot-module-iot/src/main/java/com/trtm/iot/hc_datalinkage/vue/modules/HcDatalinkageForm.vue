<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model :model="model" :rules="validatorRules" ref="form" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="运输车id" prop="truckid">
              <a-input placeholder="请输入运输车id" v-model="model.truckid" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="运输车id" prop="truckid">
              <a-input placeholder="请输入运输车id" v-model="model.truckid" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="出厂时间" prop="outstationtime">
              <a-input placeholder="请输入出厂时间" v-model="model.outstationtime" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="出厂时间" prop="outstationtime">
              <a-input placeholder="请输入出厂时间" v-model="model.outstationtime" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="卸料时间" prop="loadoffmixturetime">
              <a-input placeholder="请输入卸料时间" v-model="model.loadoffmixturetime" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="卸料时间" prop="loadoffmixturetime">
              <a-input placeholder="请输入卸料时间" v-model="model.loadoffmixturetime" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="接料时长" prop="loadmixturetime">
              <a-input placeholder="请输入接料时长" v-model="model.loadmixturetime" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="接料时长" prop="loadmixturetime">
              <a-input placeholder="请输入接料时长" v-model="model.loadmixturetime" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="摊铺记录id数组，逗号分隔" prop="pavemixtureinfoids">
              <a-input placeholder="请输入摊铺记录id数组，逗号分隔" v-model="model.pavemixtureinfoids" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="摊铺记录id数组，逗号分隔" prop="pavemixtureinfoids">
              <a-input placeholder="请输入摊铺记录id数组，逗号分隔" v-model="model.pavemixtureinfoids" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="混合料生产详情id数组，逗号分隔" prop="pavemixturematerialinfoids">
              <a-input placeholder="请输入混合料生产详情id数组，逗号分隔" v-model="model.pavemixturematerialinfoids" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="混合料生产详情id数组，逗号分隔" prop="pavemixturematerialinfoids">
              <a-input placeholder="请输入混合料生产详情id数组，逗号分隔" v-model="model.pavemixturematerialinfoids" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="混合料类型，1是水稳，2是沥青" prop="materialtype">
              <a-input-number placeholder="请输入混合料类型，1是水稳，2是沥青" style="width: 100%" v-model="model.materialtype" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="混合料类型，1是水稳，2是沥青" prop="materialtype">
              <a-input-number placeholder="请输入混合料类型，1是水稳，2是沥青" style="width: 100%" v-model="model.materialtype" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs @change="handleChangeTabs" v-model="activeKey">
      <a-tab-pane :forceRender="true" :key="refKeys[0]" tab="数据联动（摊铺数据）">
        <j-editable-table
          :actionButton="true"
          :columns="hcDatalinkagePaveTable.columns"
          :dataSource="hcDatalinkagePaveTable.dataSource"
          :disabled="formDisabled"
          :loading="hcDatalinkagePaveTable.loading"
          :maxHeight="300"
          :ref="refKeys[0]"
          :rowNumber="true"
          :rowSelection="true"/>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'HcDatalinkageForm',
    mixins: [JEditableTableModelMixin],
    components: {
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
        },
        refKeys: ['hcDatalinkagePave', ],
        tableKeys:['hcDatalinkagePave', ],
        activeKey: 'hcDatalinkagePave',
        // 数据联动（摊铺数据）
        hcDatalinkagePaveTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '机械id',
              key: 'machineid',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '机械id',
              key: 'machineid',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '机械名称',
              key: 'machinename',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '机械名称',
              key: 'machinename',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '开始摊铺时间',
              key: 'startpavetime',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '开始摊铺时间',
              key: 'startpavetime',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '结束摊铺时间',
              key: 'endpavetime',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '结束摊铺时间',
              key: 'endpavetime',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '摊铺时长',
              key: 'pavetime',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '摊铺时长',
              key: 'pavetime',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '开始摊铺桩号',
              key: 'startpavestation',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '开始摊铺桩号',
              key: 'startpavestation',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '结束摊铺桩号',
              key: 'endpavestation',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '结束摊铺桩号',
              key: 'endpavestation',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '主表id',
              key: 'zbid',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '主表id',
              key: 'zbid',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/hc_datalinkage/hcDatalinkage/add",
          edit: "/hc_datalinkage/hcDatalinkage/edit",
          queryById: "/hc_datalinkage/hcDatalinkage/queryById",
          hcDatalinkagePave: {
            list: '/hc_datalinkage/hcDatalinkage/queryHcDatalinkagePaveByMainId'
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
        this.hcDatalinkagePaveTable.dataSource=[]
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
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.hcDatalinkagePave.list, params, this.hcDatalinkagePaveTable)
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
          hcDatalinkagePaveList: allValues.tablesValue[0].values,
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
