<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbbh">
              <a-input v-model="model.sbbh" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileMileage">
              <a-input v-model="model.pileMileage" placeholder="请输入里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileNo">
              <a-input v-model="model.pileNo" placeholder="请输入桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="段号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="partid">
              <a-input v-model="model.partid" placeholder="请输入段号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="密度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDensity">
              <a-input v-model="model.pileDensity" placeholder="请输入密度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="浆量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileCement">
              <a-input v-model="model.pileCement" placeholder="请输入浆量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parttime">
              <j-date placeholder="请选择时间" v-model="model.parttime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主表uuid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oneuuid">
              <a-input v-model="model.oneuuid" placeholder="请输入主表uuid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="param1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="param1">
              <a-input v-model="model.param1" placeholder="请输入param1"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="param2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="param2">
              <a-input v-model="model.param2" placeholder="请输入param2"  ></a-input>
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
    name: 'DeviceMixpileGroutedPartForm',
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
        },
        url: {
          add: "/grouted/deviceMixpileGroutedPart/add",
          edit: "/grouted/deviceMixpileGroutedPart/edit",
          queryById: "/grouted/deviceMixpileGroutedPart/queryById"
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