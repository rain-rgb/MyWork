<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="节段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jieduan">
              <a-input v-model="model.jieduan" placeholder="请输入节段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhuanghao">
              <a-input v-model="model.zhuanghao" placeholder="请输入桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="验收内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yanshouneirong">
              <a-input v-model="model.yanshouneirong" placeholder="请输入验收内容"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报验时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baoyantime">
              <j-date placeholder="请选择报验时间" v-model="model.baoyantime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="验收时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yanshoutime">
              <j-date placeholder="请选择验收时间" v-model="model.yanshoutime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="验收单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yanshouren">
              <a-input v-model="model.yanshouren" placeholder="请输入验收单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报验单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baoyanren">
              <a-input v-model="model.baoyanren" placeholder="请输入报验单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="图片，分隔" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pics">
              <a-input v-model="model.pics" placeholder="请输入图片，分隔"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="视频" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="video">
              <a-input v-model="model.video" placeholder="请输入视频"  ></a-input>
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
    name: 'WbsYinbigongchengForm',
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
          add: "/wbsquality/wbsYinbigongcheng/add",
          edit: "/wbsquality/wbsYinbigongcheng/edit",
          queryById: "/wbsquality/wbsYinbigongcheng/queryById"
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