<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="Erp端ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['erpid', validatorRules.erpid]" placeholder="请输入Erp端ID" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="生产线(1或2)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['station']" placeholder="请输入生产线(1或2)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['code']" placeholder="请输入任务单编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="施工配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['recipe']" placeholder="请输入施工配合比编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="砂浆配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['morrec']" placeholder="请输入砂浆配合比编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择创建日期" v-decorator="['dattim']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="车辆编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['vehicle']" placeholder="请输入车辆编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="驾驶员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['driver']" placeholder="请输入驾驶员"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="生产方量(含砂浆方量)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['prodmete']" placeholder="请输入生产方量(含砂浆方量)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="砂浆方量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['mormete']" placeholder="请输入砂浆方量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="累计车次" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totvehs']" placeholder="请输入累计车次" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="累计方量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totmete']" placeholder="请输入累计方量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="质检员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['qualitor']" placeholder="请输入质检员"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['flag']" placeholder="请输入标识"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['note']" placeholder="请输入备注"  ></a-input>
            </a-form-item>
          </a-col>
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
    name: 'SchedulingCarForm',
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
          erpid: {
            rules: [
              { required: true, message: '请输入Erp端ID!'},
            ]
          },
        },
        url: {
          add: "/car/schedulingCar/add",
          edit: "/car/schedulingCar/edit",
          queryById: "/car/schedulingCar/queryById"
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
          this.form.setFieldsValue(pick(this.model,'erpid','station','code','recipe','morrec','dattim','vehicle','driver','prodmete','mormete','totvehs','totmete','qualitor','flag','note'))
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
        this.form.setFieldsValue(pick(row,'erpid','station','code','recipe','morrec','dattim','vehicle','driver','prodmete','mormete','totvehs','totmete','qualitor','flag','note'))
      },
    }
  }
</script>