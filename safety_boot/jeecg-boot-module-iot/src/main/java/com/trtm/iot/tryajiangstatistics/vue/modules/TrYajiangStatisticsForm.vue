<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="合同号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="htbh">
              <a-input v-model="model.htbh" placeholder="请输入合同号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gcmc">
              <a-input v-model="model.gcmc" placeholder="请输入工程名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="压浆时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yjsj">
              <a-input v-model="model.yjsj" placeholder="请输入压浆时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩号及部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhbw">
              <a-input v-model="model.zhbw" placeholder="请输入桩号及部位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sgbw">
              <a-input v-model="model.sgbw" placeholder="请输入施工部位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="构件结构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gjjg">
              <a-input v-model="model.gjjg" placeholder="请输入构件结构"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="构件编号及长度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gjbh">
              <a-input v-model="model.gjbh" placeholder="请输入构件编号及长度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="压浆设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yjsbbh">
              <a-input v-model="model.yjsbbh" placeholder="请输入压浆设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="梁板类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lblx">
              <a-input v-model="model.lblx" placeholder="请输入梁板类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="部门名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <a-input v-model="model.sysOrgCode" placeholder="请输入部门名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总条数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trYajiangSum">
              <a-input v-model="model.trYajiangSum" placeholder="请输入总条数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="不合格数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trYajaingOversum">
              <a-input v-model="model.trYajaingOversum" placeholder="请输入不合格数"  ></a-input>
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
    name: 'TrYajiangStatisticsForm',
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
          add: "/tryajiangstatistics/trYajiangStatistics/add",
          edit: "/tryajiangstatistics/trYajiangStatistics/edit",
          queryById: "/tryajiangstatistics/trYajiangStatistics/queryById"
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