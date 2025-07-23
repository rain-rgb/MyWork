<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeibianhao">
              <a-input v-model="model.shebeibianhao" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gongyingshangdanweibianma">
              <a-input v-model="model.gongyingshangdanweibianma" placeholder="请输入供应商"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收货单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liaocangid">
              <a-input v-model="model.liaocangid" placeholder="请输入收货单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="毛重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maozhongt">
              <a-input v-model="model.maozhongt" placeholder="请输入毛重(吨)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="皮重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pizhongt">
              <a-input v-model="model.pizhongt" placeholder="请输入皮重(吨)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="净重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jingzhongt">
              <a-input v-model="model.jingzhongt" placeholder="请输入净重(吨)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao">
              <a-input v-model="model.guigexinghao" placeholder="请输入规格类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaono">
              <a-input v-model="model.cailiaono" placeholder="请输入材料单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="车数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carsNumber">
              <a-input-number v-model="model.carsNumber" placeholder="请输入车数" style="width: 100%" />
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
    name: 'WzgbStatisticsForm',
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
          add: "/wzgbStatistics/wzgbStatistics/add",
          edit: "/wzgbStatistics/wzgbStatistics/edit",
          queryById: "/wzgbStatistics/wzgbStatistics/queryById"
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