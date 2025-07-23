<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="唯一id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fGuid">
              <a-input v-model="model.fGuid" placeholder="请输入唯一id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="syjid">
              <a-input v-model="model.syjid" placeholder="请输入试验id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fXh" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fXh">
              <a-input-number v-model="model.fXh" placeholder="请输入fXh" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="流值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liuzhi">
              <a-input-number v-model="model.liuzhi" placeholder="请输入流值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="稳定度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wendingdu">
              <a-input-number v-model="model.wendingdu" placeholder="请输入稳定度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测试时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testtime">
              <a-input v-model="model.testtime" placeholder="请输入测试时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fSbbh">
              <a-input v-model="model.fSbbh" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="isvalid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isvalid">
              <a-input v-model="model.isvalid" placeholder="请输入isvalid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="抗压力值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fYskylz">
              <a-textarea v-model="model.fYskylz" rows="4" placeholder="请输入抗压力值" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fYskyxb" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fYskyxb">
              <a-textarea v-model="model.fYskyxb" rows="4" placeholder="请输入fYskyxb" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="提交时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="submittime">
              <a-input v-model="model.submittime" placeholder="请输入提交时间"  ></a-input>
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
    name: 'StabilityDetailForm',
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
           fGuid: [
              { required: true, message: '请输入唯一id!'},
           ],
           syjid: [
              { required: true, message: '请输入试验id!'},
           ],
        },
        url: {
          add: "/StabilityDetail/stabilityDetail/add",
          edit: "/StabilityDetail/stabilityDetail/edit",
          queryById: "/StabilityDetail/stabilityDetail/queryById"
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
