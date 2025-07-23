<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-model-item label="运输车" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="truckid">
              <a-input v-model="model.truckid" placeholder="请输入运输车" ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24" >-->
<!--            <a-form-model-item label="拌和站id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stationid">-->
<!--              <a-input v-model="model.stationid" placeholder="请输入拌和站id" ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24" >
            <a-form-model-item label="拌和站名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stationname">
              <a-input v-model="model.stationname" placeholder="请输入拌和站名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="出厂时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="outstationtime">
              <j-date placeholder="请选择出厂时间" v-model="model.outstationtime" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="运输时长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="transporttime">
              <a-input v-model="model.transporttime" placeholder="请输入运输时长" ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="运输数据子表" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="hcTransportrecordsPaveTable.loading"
          :columns="hcTransportrecordsPaveTable.columns"
          :dataSource="hcTransportrecordsPaveTable.dataSource"
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

  export default {
    name: 'HcTransportrecordsForm',
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
        refKeys: ['hcTransportrecordsPave', ],
        tableKeys:['hcTransportrecordsPave', ],
        activeKey: 'hcTransportrecordsPave',
        // 运输数据子表
        hcTransportrecordsPaveTable: {
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
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '结束摊铺时间',
              key: 'endpavetime',
              type: FormTypes.date,
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
              title: '结束摊铺桩号',
              key: 'endpavestation',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/hc_transportrecords/hcTransportrecords/add",
          edit: "/hc_transportrecords/hcTransportrecords/edit",
          queryById: "/hc_transportrecords/hcTransportrecords/queryById",
          hcTransportrecordsPave: {
            list: '/hc_transportrecords/hcTransportrecords/queryHcTransportrecordsPaveByMainId'
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
        this.hcTransportrecordsPaveTable.dataSource=[]
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
          this.requestSubTableData(this.url.hcTransportrecordsPave.list, params, this.hcTransportrecordsPaveTable)
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
          hcTransportrecordsPaveList: allValues.tablesValue[0].values,
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