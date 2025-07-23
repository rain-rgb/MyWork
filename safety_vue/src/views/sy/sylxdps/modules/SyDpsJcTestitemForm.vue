<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="主键UUID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uuid">
              <a-input v-model="model.uuid" placeholder="请输入主键UUID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键（dps_jc_testItemType）试验项目类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titcode">
              <a-input v-model="model.titcode" placeholder="请输入外键（dps_jc_testItemType）试验项目类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键（dps_jc_rules）规程编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ruleno">
              <a-input v-model="model.ruleno" placeholder="请输入外键（dps_jc_rules）规程编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验项目编号/试验报告编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tino">
              <a-input v-model="model.tino" placeholder="请输入试验项目编号/试验报告编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tiname">
              <a-input v-model="model.tiname" placeholder="请输入试验项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验项目参数号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tiparameternum">
              <a-input v-model="model.tiparameternum" placeholder="请输入试验项目参数号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验项目表号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titablenum">
              <a-input v-model="model.titablenum" placeholder="请输入试验项目表号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验项目表格名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titablename">
              <a-textarea v-model="model.titablename" rows="4" placeholder="请输入试验项目表格名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验项目默认组数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tidefaultgroupnum">
              <a-input-number v-model="model.tidefaultgroupnum" placeholder="请输入试验项目默认组数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验项目排序号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tisort">
              <a-input-number v-model="model.tisort" placeholder="请输入试验项目排序号" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验项目备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tiremark">
              <a-textarea v-model="model.tiremark" rows="4" placeholder="请输入试验项目备注" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="ticustomtablenum" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ticustomtablenum">
              <a-input v-model="model.ticustomtablenum" placeholder="请输入ticustomtablenum"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="类型（0：试验项目1：报告2：评定）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titype">
              <a-input-number v-model="model.titype" placeholder="请输入类型（0：试验项目1：报告2：评定）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否删除（0：正常1：已删除）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tiisdel">
              <a-input-number v-model="model.tiisdel" placeholder="请输入是否删除（0：正常1：已删除）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="tijishuzhibiao" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tijishuzhibiao">
              <a-textarea v-model="model.tijishuzhibiao" rows="4" placeholder="请输入tijishuzhibiao" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="tizhongyaozhibiao" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tizhongyaozhibiao">
              <a-textarea v-model="model.tizhongyaozhibiao" rows="4" placeholder="请输入tizhongyaozhibiao" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="tizhongyaozhibiaoname" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tizhongyaozhibiaoname">
              <a-textarea v-model="model.tizhongyaozhibiaoname" rows="4" placeholder="请输入tizhongyaozhibiaoname" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="tibaogaotaizhangname" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tibaogaotaizhangname">
              <a-textarea v-model="model.tibaogaotaizhangname" rows="4" placeholder="请输入tibaogaotaizhangname" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="tibaogaotaizhangno" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tibaogaotaizhangno">
              <a-textarea v-model="model.tibaogaotaizhangno" rows="4" placeholder="请输入tibaogaotaizhangno" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="plusmark" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="plusmark">
              <a-input-number v-model="model.plusmark" placeholder="请输入plusmark" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="tip" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tip">
              <a-input v-model="model.tip" placeholder="请输入tip"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="tipandingyiju" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tipandingyiju">
              <a-textarea v-model="model.tipandingyiju" rows="4" placeholder="请输入tipandingyiju" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="tishiyanyiju" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tishiyanyiju">
              <a-textarea v-model="model.tishiyanyiju" rows="4" placeholder="请输入tishiyanyiju" />
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
    name: 'SyDpsJcTestitemForm',
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
           uuid: [
              { required: true, message: '请输入主键UUID!'},
           ],
           tino: [
              { required: true, message: '请输入试验项目编号/试验报告编号!'},
           ],
           tizhongyaozhibiaoname: [
              { required: true, message: '请输入tizhongyaozhibiaoname!'},
           ],
        },
        url: {
          add: "/sylxdps/syDpsJcTestitem/add",
          edit: "/sylxdps/syDpsJcTestitem/edit",
          queryById: "/sylxdps/syDpsJcTestitem/queryById"
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