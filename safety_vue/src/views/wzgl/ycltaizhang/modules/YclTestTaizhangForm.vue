<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaoname">
              <a-input v-model="model.cailiaoname" placeholder="请输入材料名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guige">
              <a-input v-model="model.guige" placeholder="请输入规格型号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料 nodetype" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nodetype">
              <a-input v-model="model.nodetype" placeholder="请输入材料 nodetype"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gongyingshang">
              <a-input v-model="model.gongyingshang" placeholder="请输入供应商名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产批号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pici">
              <a-input v-model="model.pici" placeholder="请输入生产批号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进厂时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangtime">
              <j-date placeholder="请选择进厂时间" v-model="model.jinchangtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="存放地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cunfangplace">
              <a-input v-model="model.cunfangplace" placeholder="请输入存放地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shuliang">
              <a-input v-model="model.shuliang" placeholder="请输入数量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="usepart">
              <a-input v-model="model.usepart" placeholder="请输入使用部位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="自检结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reslut">
              <a-input v-model="model.reslut" placeholder="请输入自检结果"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="自检pdf" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zjpdf">
              <a-input v-model="model.zjpdf" placeholder="请输入自检pdf"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="抽检结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cjreslut">
              <a-input v-model="model.cjreslut" placeholder="请输入抽检结果"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="抽检pdf" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cjpdf">
              <a-input v-model="model.cjpdf" placeholder="请输入抽检pdf"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="计算值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="count">
              <a-input-number v-model="model.count" placeholder="请输入计算值" style="width: 100%" />
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
    name: 'YclTestTaizhangForm',
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
          add: "/ycltesttaizhang/yclTestTaizhang/add",
          edit: "/ycltesttaizhang/yclTestTaizhang/edit",
          queryById: "/ycltesttaizhang/yclTestTaizhang/queryById"
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