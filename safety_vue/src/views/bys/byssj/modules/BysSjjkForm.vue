<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeino_dictText']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24" >
            <a-form-item label="上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gatherdate']" placeholder="无" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24" >
            <a-form-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['temperature']" placeholder="无" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24" >
            <a-form-item label="湿度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['humidity']" placeholder="无" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <!-- 子表单区域 -->
  </a-spin>
</template>

<script>

  import pick from 'lodash.pick'
  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'BysSjjkForm',
    mixins: [JEditableTableMixin],
    components: {
      JFormContainer,
      JDate,
      JDictSelectTag,
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
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
        },
        refKeys: ['yjBases', ],
        tableKeys:['yjBases', ],
        activeKey: 'yjBases',
        // 水稳材料表信息
        FYalijiTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '试件序号',
              key: 'sjxh',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            // {
            //   title: '实际配比',
            //   key: 'shijipb',
            //   type: FormTypes.normal,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
            // {
            //   title: '理论配比',
            //   key: 'lilunpb',
            //   type: FormTypes.normal,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
            {
              title: '制件编号',
              key: 'zzjj',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '抗压力值',
              key: 'kylz',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '抗压强度',
              key: 'kyqd',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '试验时间',
              key: 'sysj',
              type: FormTypes.normal,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },

            {
              title: '完成时间',
              type:FormTypes.slot,
              slotName:'wcsj',
              key: 'chaobiaodengji',
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            }
          ]
        },
        url: {
          add: "/bys/bysReal/add",
          edit: "/bys/bysReal/edit",
          queryById: "/bys/bysReal/queryById",
          SyyljBases: {
            list: '/bys/bysReal/selectById'
          },
        }
      }
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
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
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
    },
    methods: {
      addBefore(){
        this.form.resetFields()
        this.FYalijiTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'shebeino_dictText','gatherdate','temperature','humidity')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        // if (this.model.syjid) {
        //   let params = { syjid: this.model.syjid }
        //   this.requestSubTableData(this.url.SyyljBases.list, params, this.FYalijiTable)
        // }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          FYalijiTableList: allValues.tablesValue[0].values,
        }
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          })
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'shebeino_dictText','gatherdate','temperature','humidity'))
      },

    }
  }
</script>

<style scoped>
</style>