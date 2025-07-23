<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="对应试验试主表SYJID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baseid">
              <a-input v-model="model.baseid" placeholder="请输入对应试验试主表SYJID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="problemReasons">
              <a-input v-model="model.problemReasons" placeholder="请输入问题原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handleWay">
              <a-input v-model="model.handleWay" placeholder="请输入处理方式"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handleResult">
              <a-input v-model="model.handleResult" placeholder="请输入处理结果"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handleTime">
              <j-date placeholder="请选择处理时间" v-model="model.handleTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处理人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handlePerson">
              <a-input v-model="model.handlePerson" placeholder="请输入处理人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理审批" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisorApproval">
              <a-input v-model="model.supervisorApproval" placeholder="请输入监理审批"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisorResult">
              <a-input v-model="model.supervisorResult" placeholder="请输入监理结果"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisorHandleTime">
              <j-date placeholder="请选择监理处理时间" v-model="model.supervisorHandleTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="approvalPerson">
              <a-input v-model="model.approvalPerson" placeholder="请输入审批人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注(驳回原因)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-textarea v-model="model.remark" rows="4" placeholder="请输入备注(驳回原因)" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审核附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filePath">
              <a-input v-model="model.filePath" placeholder="请输入审核附件"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处置附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filePath2">
              <a-input v-model="model.filePath2" placeholder="请输入处置附件"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="超标状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="overproofStatus">
              <a-input-number v-model="model.overproofStatus" placeholder="请输入超标状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiNo">
              <a-input v-model="model.shebeiNo" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiType">
              <a-input v-model="model.shebeiType" placeholder="请输入设备类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处理数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="counts">
              <a-input-number v-model="model.counts" placeholder="请输入处理数量" style="width: 100%" />
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
    name: 'DeviceLargeOverHandlerForm',
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
           overproofStatus: [
              { required: true, message: '请输入超标状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回!'},
           ],
        },
        url: {
          add: "/deviceLargeOverHandler/deviceLargeOverHandler/add",
          edit: "/deviceLargeOverHandler/deviceLargeOverHandler/edit",
          queryById: "/deviceLargeOverHandler/deviceLargeOverHandler/queryById"
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