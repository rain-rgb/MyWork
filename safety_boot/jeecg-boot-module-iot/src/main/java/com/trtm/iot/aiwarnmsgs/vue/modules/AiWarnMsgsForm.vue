<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="报警唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="traceId">
              <a-input v-model="model.traceId" placeholder="请输入报警唯一码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cid">
              <a-input v-model="model.cid" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="摄像头名称（报警工点）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cname">
              <a-input v-model="model.cname" placeholder="请输入摄像头名称（报警工点）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报警时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warntime">
              <a-input v-model="model.warntime" placeholder="请输入报警时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报警内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warnmsg">
              <a-input v-model="model.warnmsg" placeholder="请输入报警内容"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报警类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="algtype">
              <a-input v-model="model.algtype" placeholder="请输入报警类别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="图片，多张用，分隔" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="picurls">
              <a-input v-model="model.picurls" placeholder="请输入图片，多张用，分隔"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="警报产生者" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warnent">
              <a-input v-model="model.warnent" placeholder="请输入警报产生者"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="产生者类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="enttype">
              <a-input v-model="model.enttype" placeholder="请输入产生者类别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备id 关联表shebei_info的id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiid">
              <a-input v-model="model.shebeiid" placeholder="请输入设备id 关联表shebei_info的id"  ></a-input>
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
    name: 'AiWarnMsgsForm',
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
          add: "/aiwarnmsgs/aiWarnMsgs/add",
          edit: "/aiwarnmsgs/aiWarnMsgs/edit",
          queryById: "/aiwarnmsgs/aiWarnMsgs/queryById"
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