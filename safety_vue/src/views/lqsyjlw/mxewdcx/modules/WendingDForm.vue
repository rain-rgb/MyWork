<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>

            <a-col :span="12">
              <a-form-item label="样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sampleno']" ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="采集时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['isTesttime']" ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="工程部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['gcbuwei']" ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="样品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['samplename']" ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="样品描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['samplems']" ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="流值平均值(mm)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['liuzhiavg']" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="流值标准上限值(mm)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['biaozhunzhi2']" ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="流值标准下限值(mm)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['biaozhunzhi1']" ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="稳定度标准值(KN)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['biaozhunzhi3']" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="稳定度平均值(KN)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['wddavg']" style="width: 100%" />
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

export default {
  name: 'WendingDForm',
  components: {
    JFormContainer,
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
      model: {},
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
        add: "/mxe/wendingD/add",
        edit: "/mxe/wendingD/edit",
        queryById: "/mxe/wendingD/queryById"
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
        this.form.setFieldsValue(pick(this.model,'syjid','isEnd','isTesttime','fSbbh_dictText','projectname','sampleno','gcbuwei','samplename','samplems','liuzhiavg','wddavg','biaozhunzhi1','biaozhunzhi2','biaozhunzhi3','isqualified','status','groupnum','submittime','shjieguo'))
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
      this.form.validateFields((err, values) => {
        if (!err) {
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
          httpAction(httpurl,formData,method).then((res)=>{
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
      this.form.setFieldsValue(pick(row,'syjid_dictText','isEnd','isTesttime','fSbbh','projectname','sampleno','gcbuwei','samplename','samplems','liuzhiavg','wddavg','biaozhunzhi1','biaozhunzhi2','biaozhunzhi3','isqualified','status','groupnum','submittime','shjieguo'))
    },
  }
}
</script>