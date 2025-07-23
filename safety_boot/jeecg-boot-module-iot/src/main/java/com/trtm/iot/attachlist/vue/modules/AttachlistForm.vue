<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="objectid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="objectid">
              <a-input v-model="model.objectid" placeholder="请输入objectid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="filepath" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filepath">
              <a-input v-model="model.filepath" placeholder="请输入filepath"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="createtime" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createtime">
              <j-date placeholder="请选择createtime" v-model="model.createtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="remoteip" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remoteip">
              <a-input v-model="model.remoteip" placeholder="请输入remoteip"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="filename" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filename">
              <a-input v-model="model.filename" placeholder="请输入filename"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="sign" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sign">
              <a-input v-model="model.sign" placeholder="请输入sign"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fileformat" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fileformat">
              <a-input v-model="model.fileformat" placeholder="请输入fileformat"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="userid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userid">
              <a-input v-model="model.userid" placeholder="请输入userid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="http://web.traiot.cn/docs/   路径前加地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="relativepath">
              <a-input v-model="model.relativepath" placeholder="请输入http://web.traiot.cn/docs/   路径前加地址"  ></a-input>
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
    name: 'AttachlistForm',
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
          add: "/attachlist/attachlist/add",
          edit: "/attachlist/attachlist/edit",
          queryById: "/attachlist/attachlist/queryById"
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