<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="唯一id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">
              <a-input v-model="model.guid" placeholder="请输入唯一id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="培训计划名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入培训计划名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="培训类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <a-input v-model="model.type" placeholder="请输入培训类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="startTime">
              <j-date placeholder="请选择开始时间" v-model="model.startTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endTime">
              <j-date placeholder="请选择结束时间" v-model="model.endTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="培训地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="location">
              <a-input v-model="model.location" placeholder="请输入培训地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="参与工种" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="worktype">
              <a-input v-model="model.worktype" placeholder="请输入参与工种"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="describe">
              <a-input v-model="model.describe" placeholder="请输入描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="拟参与人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="staff">
              <a-input v-model="model.staff" placeholder="请输入拟参与人员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审核状态(0待审核；10监理同意；15监理已驳回；20指挥部同意；25指挥部已驳回；30交警同意；35：交警已驳回)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="state">
              <a-input-number v-model="model.state" placeholder="请输入审核状态(0待审核；10监理同意；15监理已驳回；20指挥部同意；25指挥部已驳回；30交警同意；35：交警已驳回)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisorApproval">
              <a-input v-model="model.supervisorApproval" placeholder="请输入监理审核意见"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理审核时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisorHandleTime">
              <j-date placeholder="请选择监理审核时间" v-model="model.supervisorHandleTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指挥部审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="headquartersApproval">
              <a-input v-model="model.headquartersApproval" placeholder="请输入指挥部审核意见"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指挥部审核时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="headquartersHandleTime">
              <j-date placeholder="请选择指挥部审核时间" v-model="model.headquartersHandleTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="交警审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trafficPoliceApproval">
              <a-input v-model="model.trafficPoliceApproval" placeholder="请输入交警审核意见"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="交警审核时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trafficPoliceHandleTime">
              <j-date placeholder="请选择交警审核时间" v-model="model.trafficPoliceHandleTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="完成状态（10监理批准；20指挥部批准；30运营公司批准；40交警批准）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="completeState">
              <a-input-number v-model="model.completeState" placeholder="请输入完成状态（10监理批准；20指挥部批准；30运营公司批准；40交警批准）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="现场图片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pictureurl1">
              <a-input v-model="model.pictureurl1" placeholder="请输入现场图片"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="现场图片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pictureurl2">
              <a-input v-model="model.pictureurl2" placeholder="请输入现场图片"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="现场图片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pictureurl3">
              <a-input v-model="model.pictureurl3" placeholder="请输入现场图片"  ></a-input>
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
    name: 'GqpxTrainingPlanForm',
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
          add: "/gqpx_training_plan/gqpxTrainingPlan/add",
          edit: "/gqpx_training_plan/gqpxTrainingPlan/edit",
          queryById: "/gqpx_training_plan/gqpxTrainingPlan/queryById"
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