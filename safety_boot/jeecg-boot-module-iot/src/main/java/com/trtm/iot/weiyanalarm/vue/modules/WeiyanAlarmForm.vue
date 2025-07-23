<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="唯一ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="batchNo">
              <a-input v-model="model.batchNo" placeholder="请输入唯一ID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="围岩级别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wallRockLevel">
              <a-input v-model="model.wallRockLevel" placeholder="请输入围岩级别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测点/线" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="measuringPoint">
              <a-input v-model="model.measuringPoint" placeholder="请输入测点/线"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测量时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="birthTime">
              <j-date placeholder="请选择测量时间" v-model="model.birthTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="变形速率(mm/d)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deformationRate">
              <a-input-number v-model="model.deformationRate" placeholder="请输入变形速率(mm/d)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="累计变形量 单位（mm）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="accumulatedDeformation">
              <a-input-number v-model="model.accumulatedDeformation" placeholder="请输入累计变形量 单位（mm）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报警级别(0,代表合格，1代表黄色报警，21代表红色报警)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="callLevel">
              <a-input-number v-model="model.callLevel" placeholder="请输入报警级别(0,代表合格，1代表黄色报警，21代表红色报警)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态(0 未关闭 1 已关闭)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isUse">
              <a-input-number v-model="model.isUse" placeholder="请输入状态(0 未关闭 1 已关闭)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处置数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handleNum">
              <a-input-number v-model="model.handleNum" placeholder="请输入处置数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="workPoint">
              <a-input v-model="model.workPoint" placeholder="请输入工点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="断面名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sectionName">
              <a-input v-model="model.sectionName" placeholder="请输入断面名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工区" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="workArea">
              <a-input v-model="model.workArea" placeholder="请输入工区"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测量类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="measurementType">
              <a-input v-model="model.measurementType" placeholder="请输入测量类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处置状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dispositionStatus">
              <a-input-number v-model="model.dispositionStatus" placeholder="请输入处置状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测量人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="surveyor">
              <a-input v-model="model.surveyor" placeholder="请输入测量人员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="速率阈值(mm/d)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rateThreshold">
              <a-input v-model="model.rateThreshold" placeholder="请输入速率阈值(mm/d)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="累计阈值(mm)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cumulativeHreshold">
              <a-input v-model="model.cumulativeHreshold" placeholder="请输入累计阈值(mm)"  ></a-input>
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
    name: 'WeiyanAlarmForm',
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
           batchNo: [
              { required: true, message: '请输入唯一ID!'},
           ],
        },
        url: {
          add: "/weiyanalarm/weiyanAlarm/add",
          edit: "/weiyanalarm/weiyanAlarm/edit",
          queryById: "/weiyanalarm/weiyanAlarm/queryById"
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