<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="分层（m）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lamination">
              <a-input-number v-model="model.lamination" placeholder="请输入分层（m）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="段灰量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixpileWb">
              <a-input-number v-model="model.mixpileWb" placeholder="请输入段灰量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="段灰量得分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixpileWbscore">
              <a-input-number v-model="model.mixpileWbscore" placeholder="请输入段灰量得分" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="每点搅拌次数影响" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixpileM">
              <a-input-number v-model="model.mixpileM" placeholder="请输入每点搅拌次数影响" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="每点搅拌次数影响得分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixpileMscore">
              <a-input-number v-model="model.mixpileMscore" placeholder="请输入每点搅拌次数影响得分" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分段记分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="segmentedScoring">
              <a-input-number v-model="model.segmentedScoring" placeholder="请输入分段记分" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="合格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hege">
              <a-input v-model="model.hege" placeholder="请输入合格"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工等级判定" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mination">
              <a-input v-model="model.mination" placeholder="请输入施工等级判定"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0分层数据1总得分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stuts">
              <a-input-number v-model="model.stuts" placeholder="请输入0分层数据1总得分" style="width: 100%" />
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
    name: 'MixpileAnalysisForm',
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
          add: "/mixpileanalysis/mixpileAnalysis/add",
          edit: "/mixpileanalysis/mixpileAnalysis/edit",
          queryById: "/mixpileanalysis/mixpileAnalysis/queryById"
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