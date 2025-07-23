<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="批次号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="batchId">
              <a-input v-model="model.batchId" placeholder="请输入批次号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaoType">
              <a-input v-model="model.cailiaoType" placeholder="请输入材料类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao">
              <a-input v-model="model.guigexinghao" placeholder="请输入规格型号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="料仓" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liaocang">
              <a-input v-model="model.liaocang" placeholder="请输入料仓"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiNo">
              <a-input v-model="model.shebeiNo" placeholder="请输入设备号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指摘人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhizhairen">
              <a-input v-model="model.zhizhairen" placeholder="请输入指摘人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被指摘施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bzzSgDw">
              <a-input v-model="model.bzzSgDw" placeholder="请输入被指摘施工单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被指摘监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bzzJlDw">
              <a-input v-model="model.bzzJlDw" placeholder="请输入被指摘监理单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被指摘试验员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bzzSyy">
              <a-input v-model="model.bzzSyy" placeholder="请输入被指摘试验员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="comment">
              <a-input v-model="model.comment" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指摘时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zzTime">
              <j-date placeholder="请选择指摘时间" v-model="model.zzTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指摘类型（0：施工单位指摘 1：监理单位指摘；2：指挥部指摘）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zzType">
              <a-input v-model="model.zzType" placeholder="请输入指摘类型（0：施工单位指摘 1：监理单位指摘；2：指挥部指摘）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指摘原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zzReason">
              <a-input v-model="model.zzReason" placeholder="请输入指摘原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检验委托单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testCommissionedId">
              <a-input v-model="model.testCommissionedId" placeholder="请输入检验委托单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quliaoPic">
              <a-input v-model="model.quliaoPic" placeholder="请输入取样图"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二维码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qrCode">
              <a-input v-model="model.qrCode" placeholder="请输入二维码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二维码图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qrCodeChart">
              <a-input v-model="model.qrCodeChart" placeholder="请输入二维码图"  ></a-input>
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
    name: 'SyYuanliaoquyangweiguiForm',
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
           batchId: [
              { required: true, message: '请输入批次号!'},
           ],
        },
        url: {
          add: "/syyuanliaoquyangweigui/syYuanliaoquyangweigui/add",
          edit: "/syyuanliaoquyangweigui/syYuanliaoquyangweigui/edit",
          queryById: "/syyuanliaoquyangweigui/syYuanliaoquyangweigui/queryById"
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