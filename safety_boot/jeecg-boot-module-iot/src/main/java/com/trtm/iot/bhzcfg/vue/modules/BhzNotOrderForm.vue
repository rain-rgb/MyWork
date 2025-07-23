<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="出料日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chuliaodate">
              <j-date placeholder="请选择出料日期" v-model="model.chuliaodate"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标段名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sectionname">
              <a-input v-model="model.sectionname" placeholder="请输入标段名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="盘数（未使用浇筑令）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="panshu">
              <a-input v-model="model.panshu" placeholder="请输入盘数（未使用浇筑令）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产量（未使用浇筑令））" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="produce">
              <a-input v-model="model.produce" placeholder="请输入生产量（未使用浇筑令））"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="初级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chuji">
              <a-input v-model="model.chuji" placeholder="请输入初级超标数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="中级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhongji">
              <a-input v-model="model.zhongji" placeholder="请输入中级超标数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="高级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gaoji">
              <a-input v-model="model.gaoji" placeholder="请输入高级超标数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="problemReasons">
              <a-input v-model="model.problemReasons" placeholder="请输入问题原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="填报时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handleTime">
              <j-date placeholder="请选择填报时间" v-model="model.handleTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="填报人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handlePerson">
              <a-input v-model="model.handlePerson" placeholder="请输入填报人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批说明" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="approvalReasons">
              <j-date placeholder="请选择审批说明" v-model="model.approvalReasons"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="approvalTime">
              <j-date placeholder="请选择审批时间" v-model="model.approvalTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="approvalPerson">
              <a-input v-model="model.approvalPerson" placeholder="请输入审批人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态：0为未处理，10为施工方已处理，20为闭合" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="overproofStatus">
              <a-input-number v-model="model.overproofStatus" placeholder="请输入状态：0为未处理，10为施工方已处理，20为闭合" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fileurl">
              <a-input v-model="model.fileurl" placeholder="请输入附件"  ></a-input>
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
    name: 'BhzNotOrderForm',
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
              { required: true, message: '请输入状态：0为未处理，10为施工方已处理，20为闭合!'},
           ],
        },
        url: {
          add: "/bhzcfg/bhzNotOrder/add",
          edit: "/bhzcfg/bhzNotOrder/edit",
          queryById: "/bhzcfg/bhzNotOrder/queryById"
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