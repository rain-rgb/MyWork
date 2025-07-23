<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="批次" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pici">
              <a-input v-model="model.pici" placeholder="请输入批次"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="料仓编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liaocangno">
              <a-input-number v-model="model.liaocangno" placeholder="请输入料仓编号" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="入库数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="incount">
              <a-input v-model="model.incount" placeholder="请输入入库数量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="usenum">
              <a-input v-model="model.usenum" placeholder="请输入使用数量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaono">
              <a-input v-model="model.cailiaono" placeholder="请输入材料编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="danwei" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="danwei">
              <a-input v-model="model.danwei" placeholder="请输入danwei"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="nodetype" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nodetype">
              <a-input v-model="model.nodetype" placeholder="请输入nodetype"  ></a-input>
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
    name: 'WztaizhangLcForm',
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
          add: "/wztaizhang/wztaizhangLc/add",
          edit: "/wztaizhang/wztaizhangLc/edit",
          queryById: "/wztaizhang/wztaizhangLc/queryById"
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