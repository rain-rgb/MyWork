<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="年度如 2024年度研判" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="niandu">
              <a-input v-model="model.niandu" placeholder="请输入年度如 2024年度研判"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectname">
              <a-input v-model="model.projectname" placeholder="请输入项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属业务部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="departname">
              <a-input v-model="model.departname" placeholder="请输入所属业务部门"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="guid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">
              <a-input v-model="model.guid" placeholder="请输入guid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="说明 备注一栏根据对问题进行归类，如工程质量、施工安全、文明施工、内业资料、综合管理等" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入说明 备注一栏根据对问题进行归类，如工程质量、施工安全、文明施工、内业资料、综合管理等"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="整改单位（责任单位）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhenggaiUnit">
              <a-input v-model="model.zhenggaiUnit" placeholder="请输入整改单位（责任单位）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="整改责任人【暂不使用】" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhengaiPeople">
              <a-input v-model="model.zhengaiPeople" placeholder="请输入整改责任人【暂不使用】"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="整改情况 整改情况应填写“已整改”或“整改中”，不得采用其他描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhenggaiInfo">
              <a-input v-model="model.zhenggaiInfo" placeholder="请输入整改情况 整改情况应填写“已整改”或“整改中”，不得采用其他描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工点名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="manageLocation">
              <a-input v-model="model.manageLocation" placeholder="请输入工点名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="验收单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yanshouUnit">
              <a-input v-model="model.yanshouUnit" placeholder="请输入验收单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="整改期限年月日（完成期限）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhenggaiTime">
              <a-input v-model="model.zhenggaiTime" placeholder="请输入整改期限年月日（完成期限）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="发现问题内容 问题简要描述应具体扼要，原则上一行一序号且只描述1个问题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="problems">
              <a-input v-model="model.problems" placeholder="请输入发现问题内容 问题简要描述应具体扼要，原则上一行一序号且只描述1个问题"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="整改措施和要求" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhenggaiRequire">
              <a-input v-model="model.zhenggaiRequire" placeholder="请输入整改措施和要求"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检查人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="checkPeople">
              <a-input v-model="model.checkPeople" placeholder="请输入检查人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检查年月日" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="checkTime">
              <a-input v-model="model.checkTime" placeholder="请输入检查年月日"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="问题类别： ⼀般问题；⼀般隐患；重⼤隐患" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="problemType">
              <a-input v-model="model.problemType" placeholder="请输入问题类别： ⼀般问题；⼀般隐患；重⼤隐患"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="治理要求" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="procedure1Info">
              <a-input v-model="model.procedure1Info" placeholder="请输入治理要求"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="procedure1Reslut" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="procedure1Reslut">
              <a-input v-model="model.procedure1Reslut" placeholder="请输入procedure1Reslut"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签发人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="procedure1People">
              <a-input v-model="model.procedure1People" placeholder="请输入签发人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检查年月日" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="procedure1Time">
              <a-input v-model="model.procedure1Time" placeholder="请输入检查年月日"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="procedure2Reslut" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="procedure2Reslut">
              <a-input v-model="model.procedure2Reslut" placeholder="请输入procedure2Reslut"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接收人（整改责任人）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="procedure2People">
              <a-input v-model="model.procedure2People" placeholder="请输入接收人（整改责任人）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接收时间年月日" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="procedure2Time">
              <a-input v-model="model.procedure2Time" placeholder="请输入接收时间年月日"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zlsNo">
              <a-input v-model="model.zlsNo" placeholder="请输入编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否销号应填写“是”或“否”，不得采用其他描述。" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isover">
              <a-input-number v-model="model.isover" placeholder="请输入是否销号应填写“是”或“否”，不得采用其他描述。" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="隐患类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yhlb">
              <a-input v-model="model.yhlb" placeholder="请输入隐患类别"  ></a-input>
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
    name: 'AnquanFxaqjcInfoForm',
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
          add: "/anquanfxgk/anquanFxaqjcInfo/add",
          edit: "/anquanfxgk/anquanFxaqjcInfo/edit",
          queryById: "/anquanfxgk/anquanFxaqjcInfo/queryById"
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