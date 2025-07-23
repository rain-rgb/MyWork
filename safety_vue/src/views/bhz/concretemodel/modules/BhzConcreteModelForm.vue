<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode" ::multi="false"  />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模具编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
              <a-input v-model="model.code" placeholder="请输入模具编码" aria-required="true" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模具名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入试块模具名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guige">
<!--              <a-input v-model="model.guige" placeholder="请输入规格"  ></a-input>-->
              <j-dict-select-tag placeholder="请选择规格" v-model="model.guige"
                                 dictCode="mjgg"></j-dict-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材质" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chicun">
<!--              <a-input v-model="model.chicun" placeholder="请输入材质"  ></a-input>-->
              <j-dict-select-tag placeholder="请选择规格" v-model="model.chicun"
                                 dictCode="mjcz"></j-dict-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入说明"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'BhzConcreteModelForm',
    components: {
      JselectDqDepart
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
          add: "/bhzconcretemodel/bhzConcreteModel/add",
          edit: "/bhzconcretemodel/bhzConcreteModel/edit",
          queryById: "/bhzconcretemodel/bhzConcreteModel/queryById"
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