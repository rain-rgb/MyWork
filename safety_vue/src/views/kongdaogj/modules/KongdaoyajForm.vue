<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-model-item label="项目所在省份" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="province">
              <a-input v-model="model.province" placeholder="请输入项目所在省份" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="项目所属行业" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="detectindustry">
              <a-input v-model="model.detectindustry" placeholder="请输入项目所属行业" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测性质" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="detectnaturenum">
              <a-input v-model="model.detectnaturenum" placeholder="请输入检测性质" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="detecttype">
              <a-input v-model="model.detecttype" placeholder="请输入检测类型" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="detectunit">
              <a-input v-model="model.detectunit" placeholder="请输入检测单位" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="设备厂商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devicecompany">
              <a-input v-model="model.devicecompany" placeholder="请输入设备厂商" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" prop="remarks">
              <a-textarea v-model="model.remarks" rows="4" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="工程地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projplace">
              <a-input v-model="model.projplace" placeholder="请输入工程地点" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="workname">
              <a-input v-model="model.workname" placeholder="请输入工程名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="detectperson">
              <a-input v-model="model.detectperson" placeholder="请输入检测人员" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测大类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="detectparam">
              <a-input v-model="model.detectparam" placeholder="请输入检测大类" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="检测小类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="detectclass">
              <a-input v-model="model.detectclass" placeholder="请输入检测小类" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="consunit">
              <a-input v-model="model.consunit" placeholder="请输入施工单位" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisionunit">
              <a-input v-model="model.supervisionunit" placeholder="请输入监理单位" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="建设单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="employer">
              <a-input v-model="model.employer" placeholder="请输入建设单位" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="试验编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="detectno">
              <a-input v-model="model.detectno" placeholder="请输入试验编号" ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="孔道灌浆子表" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="kongdaoyajsTable.loading"
          :columns="kongdaoyajsTable.columns"
          :dataSource="kongdaoyajsTable.dataSource"
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
    name: 'KongdaoyajForm',
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
           uuid: [
              { required: true, message: '请输入json文件唯一标识!'},
           ],
        },
        refKeys: ['kongdaoyajs', ],
        tableKeys:['kongdaoyajs', ],
        activeKey: 'kongdaoyajs',
        // 孔道灌浆子表
        kongdaoyajsTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '数据内容',
              key: 'detectdata',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '检测内容',
              key: 'detectitem',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '检测编号',
              key: 'detectitemnum',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '解析编号',
              key: 'analysisnum',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '检测日期',
              key: 'detectdate',
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '构件编号',
              key: 'componentid',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '构件名称',
              key: 'componentname',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '构件位置',
              key: 'detectlocation',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '数据文件名称',
              key: 'filename',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            // {
            //   title: 'json文件唯一标识',
            //   key: 'uuid',
            //   type: FormTypes.input,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
          ]
        },
        url: {
          add: "/kongdaoyaj/kongdaoyaj/add",
          edit: "/kongdaoyaj/kongdaoyaj/edit",
          queryById: "/kongdaoyaj/kongdaoyaj/queryById",
          kongdaoyajs: {
            list: '/kongdaoyaj/kongdaoyaj/queryKongdaoyajsByMainId'
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
        this.kongdaoyajsTable.dataSource=[]
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
          let params = { uuid: this.model.uuid }
          this.requestSubTableData(this.url.kongdaoyajs.list, params, this.kongdaoyajsTable)
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
          kongdaoyajsList: allValues.tablesValue[0].values,
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