<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="车牌号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="card">
              <a-input v-model="model.card" placeholder="请输入车牌号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进入时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="enterTime">
              <a-input v-model="model.enterTime" placeholder="请输入进入时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出去时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="outTime">
              <a-input v-model="model.outTime" placeholder="请输入出去时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <a-input v-model="model.shebeino" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="唯一标识" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">
              <a-input v-model="model.guid" placeholder="请输入唯一标识"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="左右隧道标志 1 左洞  2右洞" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lorr">
              <a-input-number v-model="model.lorr" placeholder="请输入左右隧道标志 1 左洞  2右洞" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="隧道进出标志 1 进洞  2 出洞" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ioflag">
              <a-input-number v-model="model.ioflag" placeholder="请输入隧道进出标志 1 进洞  2 出洞" style="width: 100%" />
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
    name: 'SafetyTunnelCarHistoryForm',
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
           shebeino: [
              { required: true, message: '请输入设备编号!'},
           ],
           guid: [
              { required: true, message: '请输入唯一标识!'},
           ],
        },
        url: {
          add: "/safetyTunnelCarHistory/safetyTunnelCarHistory/add",
          edit: "/safetyTunnelCarHistory/safetyTunnelCarHistory/edit",
          queryById: "/safetyTunnelCarHistory/safetyTunnelCarHistory/queryById"
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