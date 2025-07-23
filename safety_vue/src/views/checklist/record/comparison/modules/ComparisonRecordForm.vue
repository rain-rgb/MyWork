<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="优胜名单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="namewin">
              <a-textarea v-model="model.namewin" placeholder="请输入优胜名单"  ></a-textarea>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="优良名单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nameelle">
              <a-textarea v-model="model.nameelle" placeholder="请输入优良名单"  ></a-textarea>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="达标名单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="namestan">
              <a-textarea v-model="model.namestan" placeholder="请输入达标名单"  ></a-textarea>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="季度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quarter">
              <j-dict-select-tag type="list" v-model="model.quarter" dictCode="quarter" placeholder="请选择季度" />
<!--              <a-input-number v-model="model.quarter" placeholder="请输入季度" style="width: 100%" />-->
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="优胜数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="winningnum">-->
<!--              <a-input-number v-model="model.winningnum" placeholder="请输入优胜数" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="优良数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="excellentnum">-->
<!--              <a-input-number v-model="model.excellentnum" placeholder="请输入优良数" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="达标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="standardnum">-->
<!--              <a-input-number v-model="model.standardnum" placeholder="请输入达标数" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="评比状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <j-dict-select-tag type="list" v-model="model.status" dictCode="pingbistatus" placeholder="请选择评比状态" />
              <!--              <a-input-number v-model="model.quarter" placeholder="请输入季度" style="width: 100%" />-->
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
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'

  export default {
    name: 'ComparisonRecordForm',
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
          add: "/comparisonrecord/comparisonRecord/add",
          edit: "/comparisonrecord/comparisonRecord/edit",
          queryById: "/comparisonrecord/comparisonRecord/queryById"
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