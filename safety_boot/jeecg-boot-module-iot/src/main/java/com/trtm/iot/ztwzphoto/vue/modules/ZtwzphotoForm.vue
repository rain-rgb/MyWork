<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgid">
              <a-input v-model="model.orgid" placeholder="请输入组织机构"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主表主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderid">
              <a-input v-model="model.orderid" placeholder="请输入主表主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="相机位置" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cameraposition">
              <a-input v-model="model.cameraposition" placeholder="请输入相机位置"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="相机编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cameracode">
              <a-input v-model="model.cameracode" placeholder="请输入相机编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="照片类型（入场、出场）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phototype">
              <a-input v-model="model.phototype" placeholder="请输入照片类型（入场、出场）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="照片链接Key" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="photourl">
              <a-input v-model="model.photourl" placeholder="请输入照片链接Key"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上传之前端上的主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriitemid">
              <a-input v-model="model.oriitemid" placeholder="请输入上传之前端上的主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上传之前端上的主表主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriorderid">
              <a-input v-model="model.oriorderid" placeholder="请输入上传之前端上的主表主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="删除状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isremoved">
              <a-input v-model="model.isremoved" placeholder="请输入删除状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上传状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="externaluploadstate">
              <a-input v-model="model.externaluploadstate" placeholder="请输入上传状态"  ></a-input>
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
    name: 'ZtwzphotoForm',
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
          add: "/ztwzphoto/ztwzphoto/add",
          edit: "/ztwzphoto/ztwzphoto/edit",
          queryById: "/ztwzphoto/ztwzphoto/queryById"
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