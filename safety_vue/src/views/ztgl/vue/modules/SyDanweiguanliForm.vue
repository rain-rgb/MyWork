<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
                    <a-col :span="24">
                      <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">
                        <JselectDqDepart v-model="model.sysorgcode"   ::multi="false"  />
                      </a-form-item>
                    </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="所属机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">-->
<!--              <a-input v-model="model.sysorgcode" placeholder="请输入所属机构"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="单位名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dwname">
              <a-input v-model="model.dwname" placeholder="请输入单位名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单位类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dwtype">
              <a-input-number v-model="model.dwtype" placeholder="请输入单位类型（施工单位；监理单位；指挥部；实验室；外委单位；辅助员）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="资质证书编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zzzs">
              <a-input v-model="model.zzzs" placeholder="请输入自治证书编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="file">
              <j-image-upload v-model="model.file" :isMultiple="isMultiple" ></j-image-upload>
            </a-form-item>
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
    name: 'SyDanweiguanliForm',
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
          add: "/syztgl/syDanweiguanli/add",
          edit: "/syztgl/syDanweiguanli/edit",
          queryById: "/syztgl/syDanweiguanli/queryById"
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