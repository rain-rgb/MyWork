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
            <a-form-model-item label="试验类型编码(两位编码01开始)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titcode">
              <a-input v-model="model.titcode" placeholder="请输入试验类型编码(两位编码01开始)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验类型名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titname">
              <a-input v-model="model.titname" placeholder="请输入试验类型名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品标识" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titsamplemark">
              <a-input v-model="model.titsamplemark" placeholder="请输入样品标识"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验类型父节点编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titparentcode">
              <a-input v-model="model.titparentcode" placeholder="请输入试验类型父节点编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="当前节点的子节点个数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titchildnodesnum">
              <a-input v-model="model.titchildnodesnum" placeholder="请输入当前节点的子节点个数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验类型备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titremark">
              <a-textarea v-model="model.titremark" rows="4" placeholder="请输入试验类型备注" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否删除（0：正常1：已删除）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titisdel">
              <a-input-number v-model="model.titisdel" placeholder="请输入是否删除（0：正常1：已删除）" style="width: 100%" />
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
          <a-col :span="24">
            <a-form-model-item label="tittype" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tittype">
              <a-input-number v-model="model.tittype" placeholder="请输入tittype" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="记录表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容 格式（, |）：样品名称,sampleName|样品描述,sampleDescribe" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tiyangpinxinxijl">
              <a-textarea v-model="model.tiyangpinxinxijl" rows="4" placeholder="请输入记录表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容 格式（, |）：样品名称,sampleName|样品描述,sampleDescribe" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tiyangpinxinxibg">
              <a-textarea v-model="model.tiyangpinxinxibg" rows="4" placeholder="请输入报告表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="tipushtablename" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tipushtablename">
              <a-input v-model="model.tipushtablename" placeholder="请输入tipushtablename"  ></a-input>
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
    name: 'SyDpsJcTestitemtypeForm',
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
        },
        url: {
          add: "/sylxdps/syDpsJcTestitemtype/add",
          edit: "/sylxdps/syDpsJcTestitemtype/edit",
          queryById: "/sylxdps/syDpsJcTestitemtype/queryById"
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