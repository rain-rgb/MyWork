<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="软基工单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rjrwd">
              <a-input v-model="model.rjrwd" placeholder="请输入软基工单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgcode">
              <a-input v-model="model.orgcode" placeholder="请输入组织机构"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <a-input v-model="model.shebeino" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mileage">
              <a-input v-model="model.mileage" placeholder="请输入里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计桩基数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="descount">
              <a-input-number v-model="model.descount" placeholder="请输入设计桩基数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开工日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="starttime">
              <j-date placeholder="请选择开工日期" v-model="model.starttime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="截止日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endtime">
              <j-date placeholder="请选择截止日期" v-model="model.endtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="rjstatus" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rjstatus">
              <a-input-number v-model="model.rjstatus" placeholder="请输入rjstatus" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="totalpro" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalpro">
              <a-input-number v-model="model.totalpro" placeholder="请输入totalpro" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="mileagetotal" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mileagetotal">
              <a-input-number v-model="model.mileagetotal" placeholder="请输入mileagetotal" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="chaobiaototal" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chaobiaototal">
              <a-input-number v-model="model.chaobiaototal" placeholder="请输入chaobiaototal" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="handled" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handled">
              <a-input-number v-model="model.handled" placeholder="请输入handled" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="checked" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="checked">
              <a-input-number v-model="model.checked" placeholder="请输入checked" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="chaobiaolv" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chaobiaolv">
              <a-input-number v-model="model.chaobiaolv" placeholder="请输入chaobiaolv" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'DeviceMixpileRwdstaForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
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
           mileagetotal: [
              { required: true, message: '请输入mileagetotal!'},
           ],
        },
        url: {
          add: "/devicemixpilerwdsta/deviceMixpileRwdsta/add",
          edit: "/devicemixpilerwdsta/deviceMixpileRwdsta/edit",
          queryById: "/devicemixpilerwdsta/deviceMixpileRwdsta/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
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
    }
  }
</script>