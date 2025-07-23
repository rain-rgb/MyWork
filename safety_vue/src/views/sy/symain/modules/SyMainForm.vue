<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="唯一id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uuid">
              <a-input v-model="model.uuid" placeholder="请输入唯一id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="组织结构代码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">
              <a-input v-model="model.sysorgcode" placeholder="请输入组织结构代码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deviceId">
              <a-input v-model="model.deviceId" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品封签照片（图片）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fqzpstart">
              <a-input v-model="model.fqzpstart" placeholder="请输入样品封签照片（图片）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="留样封签照片（图片）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fqzpend">
              <a-input v-model="model.fqzpend" placeholder="请输入留样封签照片（图片）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="仪器校准照片（图片）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yqjzzp">
              <a-input v-model="model.yqjzzp" placeholder="请输入仪器校准照片（图片）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测环境照片（图片）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jchjzp">
              <a-input v-model="model.jchjzp" placeholder="请输入检测环境照片（图片）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验类型，与dps_jc_testItemType表titCode关联" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testType">
              <a-input v-model="model.testType" placeholder="请输入试验类型，与dps_jc_testItemType表titCode关联"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测标准" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testStandard">
              <a-input v-model="model.testStandard" placeholder="请输入检测标准"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测状态（0：未开始，1：准备中，2：试验中，3：已结束，10：合格，11：不合格，默认为0）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testStatus">
              <a-input-number v-model="model.testStatus" placeholder="请输入检测状态（0：未开始，1：准备中，2：试验中，3：已结束，10：合格，11：不合格，默认为0）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验结果（0：合格，1：不合格，暂不使用）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testResult">
              <a-input-number v-model="model.testResult" placeholder="请输入试验结果（0：合格，1：不合格，暂不使用）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测数据（PDF文件）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testData">
              <a-input v-model="model.testData" placeholder="请输入检测数据（PDF文件）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="摄像头id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cameraId">
              <a-input v-model="model.cameraId" placeholder="请输入摄像头id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检验批" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="inspectionIot">
              <a-input v-model="model.inspectionIot" placeholder="请输入检验批"  ></a-input>
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
    name: 'SyMainForm',
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
          add: "/syMain/syMain/add",
          edit: "/syMain/syMain/edit",
          queryById: "/syMain/syMain/queryById"
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
