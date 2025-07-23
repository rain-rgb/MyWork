<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="用户id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['empid', validatorRules.empid]" placeholder="请输入用户id" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="部门id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['devid']" placeholder="请输入部门id" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="进入时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['entersitetime']" placeholder="请输入进入时间"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="进入隧道时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['entersdtime']" placeholder="请输入进入隧道时间"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="other1" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['other1']" placeholder="请输入other1"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="other2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['other2']" placeholder="请输入other2"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="other3" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['other3']" placeholder="请输入other3"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="tagid" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tagid']" placeholder="请输入tagid"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="用户名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['empname']" placeholder="请输入用户名"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="隧道名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['deptname']" placeholder="请输入隧道名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="班组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jobname']" placeholder="请输入班组"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="位置名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['devname']" placeholder="请输入位置名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="isinside" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['isinside']" placeholder="请输入isinside"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="路线名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['tunnelid']" placeholder="请输入路线名称" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="deptid" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['deptid']" placeholder="请输入deptid" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeino']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="路线名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tunnelname']" placeholder="请输入路线名称"  ></a-input>
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

  export default {
    name: 'DeviceEmplocationForm',
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
          empid: {
            rules: [
              { required: true, message: '请输入用户id!'},
            ]
          },
        },
        url: {
          add: "/DeviceEmplocation/deviceEmplocation/add",
          edit: "/DeviceEmplocation/deviceEmplocation/edit",
          queryById: "/DeviceEmplocation/deviceEmplocation/queryById"
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
          this.form.setFieldsValue(pick(this.model,'empid','devid','entersitetime','entersdtime','other1','other2','other3','tagid','empname','deptname','jobname','devname','isinside','tunnelid','deptid','shebeino','tunnelname'))
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
        this.form.setFieldsValue(pick(row,'empid','devid','entersitetime','entersdtime','other1','other2','other3','tagid','empname','deptname','jobname','devname','isinside','tunnelid','deptid','shebeino','tunnelname'))
      },
    }
  }
</script>