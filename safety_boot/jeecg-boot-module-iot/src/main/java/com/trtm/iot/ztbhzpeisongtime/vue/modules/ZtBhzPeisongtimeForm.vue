<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="工程类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="engineeringtype">
              <a-input-number v-model="model.engineeringtype" placeholder="请输入工程类型" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单位工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="engineering">
              <a-input v-model="model.engineering" placeholder="请输入单位工程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="基准运距 （Km)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="distance">
              <a-input v-model="model.distance" placeholder="请输入基准运距 （Km)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="基准考核时间（分钟）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="assessmenttime">
              <a-input v-model="model.assessmenttime" placeholder="请输入基准考核时间（分钟）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="隧道洞口里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mileage">
              <a-input v-model="model.mileage" placeholder="请输入隧道洞口里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="月末里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endmileage">
              <a-input v-model="model.endmileage" placeholder="请输入月末里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="增加运距 （Km)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gddistance">
              <a-input v-model="model.gddistance" placeholder="请输入增加运距 （Km)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="增加考核时间（分钟）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gdassessmenttime">
              <a-input v-model="model.gdassessmenttime" placeholder="请输入增加考核时间（分钟）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="综合运距 （Km)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhdistance">
              <a-input v-model="model.zhdistance" placeholder="请输入综合运距 （Km)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="本期考核时间 （分钟）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bqassessmenttime">
              <a-input v-model="model.bqassessmenttime" placeholder="请输入本期考核时间 （分钟）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入备注"  ></a-input>
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
    name: 'ZtBhzPeisongtimeForm',
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
          add: "/ztbhzpeisongtime/ztBhzPeisongtime/add",
          edit: "/ztbhzpeisongtime/ztBhzPeisongtime/edit",
          queryById: "/ztbhzpeisongtime/ztBhzPeisongtime/queryById"
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