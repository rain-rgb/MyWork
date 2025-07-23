<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cid">
              <a-input-number v-model="model.cid" placeholder="请输入设备编号" style="width: 100%" />
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
        </a-row>
          <a-tabs default-active-key="1">
            <a-tab-pane key="1" tab="图片展示">
              <viewer  >
                <img  style="height:450px;width: 550px;margin: 5px 10px 5px 10px;float: left"
                  :src="model.picurls"
                  alt="">
              </viewer>
            </a-tab-pane>
          </a-tabs>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'AiWarnMsgForm',
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
          add: "/aiwarnmsg/aiWarnMsg/add",
          edit: "/aiwarnmsg/aiWarnMsg/edit",
          queryById: "/aiwarnmsg/aiWarnMsg/queryById"
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