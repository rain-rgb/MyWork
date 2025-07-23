<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['shebeino_dictText']" placeholder="请输入设备名称"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pileno']" placeholder="请输入桩号"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设计桩长" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['designdep']" placeholder="请输入设计桩长"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="当前桩长度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['curdep']" placeholder="请输入当前桩长度"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="离地桩长" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['grounddep']" placeholder="请输入离地桩长"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="最大垂直度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pileY']" placeholder="请输入最大垂直度"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="最大压桩力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pileUpress']" placeholder="请输入最大压桩力"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="最大夹持力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pileDpress']" placeholder="请输入最大夹持力"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="平均桩压力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pileAveupress']" placeholder="请输入平均桩压力"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="平均夹持力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pileAvedpress']" placeholder="请输入平均夹持力"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pileStarttime']" placeholder="请输入开始时间"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择数据时间" v-decorator="['datatime']" :trigger-change="true" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="接桩时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['piletime']" placeholder="请输入接桩时间"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="打桩次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['times']" placeholder="请输入打桩次数"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: 'DevicePipepileHistorydataForm',
    components: {
      JFormContainer,
      JDate,
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
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
    data () {
      return {
        form: this.$form.createForm(this),
        model:{},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/devicepipepilehistorydata/devicePipepileHistorydata/add",
          edit: "/devicepipepilehistorydata/devicePipepileHistorydata/edit",
          queryById: "/devicepipepilehistorydata/devicePipepileHistorydata/queryById"
        }
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
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'shebeino_dictText','pileno','designdep','curdep','grounddep','pileY','pileUpress','pileDpress','pileAveupress','pileAvedpress','pileStarttime','datatime','piletime','times'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'shebeino_dictText','pileno','designdep','curdep','grounddep','pileY','pileUpress','pileDpress','pileAveupress','pileAvedpress','pileStarttime','datatime','piletime','times'))
      },
    }
  }
</script>