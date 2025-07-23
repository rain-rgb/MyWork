<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row v-if="process === 0 || process === 3 ">
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="对应成桩记录主表id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baseid">-->
<!--              <a-input v-model="model.baseid" placeholder="请输入对应成桩记录主表id"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbbh">-->
<!--              <a-input v-model="model.sbbh" placeholder="请输入设备编号"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileno">-->
<!--              <a-input-number v-model="model.pileno" placeholder="请输入桩号" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="problemReasons">
              <a-input v-model="model.problemReasons" placeholder="请输入问题原因" :disabled="chuzhidis" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handleWay">
              <a-input v-model="model.handleWay" placeholder="请输入处理方式" :disabled="chuzhidis"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handleResult">
              <a-input v-model="model.handleResult" placeholder="请输入处理结果" :disabled="chuzhidis" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="process === 3" >
            <a-form-model-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handleTime">
              <j-date placeholder="请选择处理时间" v-model="model.handleTime" read-only style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="process === 3" >
            <a-form-model-item label="处理人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handlePerson">
              <a-input v-model="model.handlePerson" placeholder="请输入处理人" disabled="true" ></a-input>
            </a-form-model-item>
          </a-col>



          <a-col :span="24">
            <a-form-item label="上传处置附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filePath2">
              <j-image-upload v-model="model.filePath2" :isMultiple="isMultiple" ></j-image-upload>
            </a-form-item>
<!--            <a-form-model-item label="处置附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filePath2">-->
<!--              <a-input v-model="model.filePath2" placeholder="请输入处置附件"  ></a-input>-->
<!--            </a-form-model-item>-->
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="超标状态：0为未处理，10为施工方已处理，20为监理方已处理" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="overproofStatus">-->
<!--              <a-input-number v-model="model.overproofStatus" placeholder="请输入超标状态：0为未处理，10为施工方已处理，20为监理方已处理" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
        </a-row >
        <a-row  v-if="process === 1" >
          <a-col :span="24">
            <a-form-model-item label="备注(驳回)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-textarea v-model="model.remark" rows="4" placeholder="请输入备注(驳回)" :disabled="bohuidis" />
            </a-form-model-item>
          </a-col>
        </a-row>

        <a-row  v-if="process === 2 || process === 3 " >
          <a-col :span="24">
            <a-form-model-item label="监理审批" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisorApproval">
              <a-input v-model="model.supervisorApproval" placeholder="请输入监理审批" :disabled="shenhe" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisorResult">
              <a-input v-model="model.supervisorResult" placeholder="请输入监理结果" :disabled="shenhe"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24"  v-if=" process === 3 ">
            <a-form-model-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisorHandleTime">
              <j-date placeholder="请选择监理处理时间" v-model="model.supervisorHandleTime"  style="width: 100%" readOnly />
            </a-form-model-item>
          </a-col>
          <a-col :span="24"  v-if=" process === 3 ">
            <a-form-model-item label="审批人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="approvalPerson">
              <a-input v-model="model.approvalPerson" placeholder="请输入审批人" disabled="true" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="上传审核附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filePath">
              <j-image-upload v-model="model.filePath" :isMultiple="isMultiple" ></j-image-upload>
            </a-form-item>
<!--            <a-form-model-item label="审核附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filePath">-->
<!--              <a-input v-model="model.filePath" placeholder="请输入审核附件"  ></a-input>-->
<!--            </a-form-model-item>-->
          </a-col>
        </a-row>

      </a-form-model>

    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import JImageUpload from "@comp/jeecg/JImageUpload";

  export default {
    name: 'MixpileOneOverHandlerForm',
    components: {JImageUpload
    },
    watch:{
      process(data){
        if(data===0){
          this.chuzhidis = false
        }else if(data === 1){
          this.bohuidis = false
        }else if(data === 2){
          this.shenhe = false
        }else{

        }
      }
    },
    mounted() {
      if(this.process===0){
        this.chuzhidis = false
      }else if(this.process === 1){
        this.bohuidis = false
      }else if(this.process === 2){
        this.shenhe = false
      }else{

      }
      },
    props: {
      handlestate:0,
      process:0,// 0处置，1驳回，2审核，3查看

      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        isMultiple : true,
        chuzhidis:true,
        bohuidis:true,
        shenhe:true,
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
          add: "/mixpileoneoverhandler/mixpileOneOverHandler/handle",
          edit: "/mixpileoneoverhandler/mixpileOneOverHandler/editNext",
          queryById: "/mixpileoneoverhandler/mixpileOneOverHandler/queryById"
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
      add (record) {
        this.edit(record);

      },
      edit (record) {
        this.model = Object.assign({}, record.handler);
        this.model.filePath3 = "";
        let idsArr = [];
        record.deviceMixpileOneHandlerPages.forEach((e) => {
          idsArr.push(e.id);
        });
        this.model.filePath3 = idsArr.toString();
        
        if(this.process===0){
          this.model.baseid =  record.id;
          this.model.sbbh = record.shebeino;
          this.model.pileno = record.pileNo;
          this.model.overproofStatus = 10
        }else if(this.process === 1){
          this.model.overproofStatus = 30
        }else if(this.process === 2){
          this.model.overproofStatus = 20
        }else{

        }
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
            if(this.model.overproofStatus == 10){
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