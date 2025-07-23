<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="料仓id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="storageId">
              <a-input v-model="model.storageId" placeholder="请输入料仓id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="料仓id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="storageId">
              <a-input v-model="model.storageId" placeholder="请输入料仓id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检验批编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="inspectionLotNumber">
              <a-input v-model="model.inspectionLotNumber" placeholder="请输入检验批编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检验批编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="inspectionLotNumber">
              <a-input v-model="model.inspectionLotNumber" placeholder="请输入检验批编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleNumber">
              <a-input v-model="model.sampleNumber" placeholder="请输入样品编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleNumber">
              <a-input v-model="model.sampleNumber" placeholder="请输入样品编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testName">
              <a-input v-model="model.testName" placeholder="请输入试验名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testName">
              <a-input v-model="model.testName" placeholder="请输入试验名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplingTime">
              <j-date placeholder="请选择取样时间" v-model="model.samplingTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplingTime">
              <j-date placeholder="请选择取样时间" v-model="model.samplingTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testTime">
              <j-date placeholder="请选择试验时间" v-model="model.testTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testTime">
              <j-date placeholder="请选择试验时间" v-model="model.testTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验结论" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conclusion">
              <a-input v-model="model.conclusion" placeholder="请输入试验结论"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验结论" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conclusion">
              <a-input v-model="model.conclusion" placeholder="请输入试验结论"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tester">
              <a-input v-model="model.tester" placeholder="请输入试验员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tester">
              <a-input v-model="model.tester" placeholder="请输入试验员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告详情" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="report">
              <a-input v-model="model.report" placeholder="请输入报告详情"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告详情" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="report">
              <a-input v-model="model.report" placeholder="请输入报告详情"  ></a-input>
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
    name: 'YclTestDetailForm',
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
          add: "/ycltd/yclTestDetail/add",
          edit: "/ycltd/yclTestDetail/edit",
          queryById: "/ycltd/yclTestDetail/queryById"
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
