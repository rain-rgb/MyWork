<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="syjid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="syjid">
              <a-input v-model="model.syjid" placeholder="请输入syjid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fxh" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fxh">
              <a-input v-model="model.fxh" placeholder="请输入fxh"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="yandu1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yandu1">
              <a-input v-model="model.yandu1" placeholder="请输入yandu1"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="yandu2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yandu2">
              <a-input v-model="model.yandu2" placeholder="请输入yandu2"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="yandu3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yandu3">
              <a-input v-model="model.yandu3" placeholder="请输入yandu3"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="testtime" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testtime">
              <j-date placeholder="请选择testtime" v-model="model.testtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fWtbh" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fWtbh">
              <a-input v-model="model.fWtbh" placeholder="请输入fWtbh"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fsbbh" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fsbbh">
              <a-input v-model="model.fsbbh" placeholder="请输入fsbbh"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fSjbh" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fSjbh">
              <a-input v-model="model.fSjbh" placeholder="请输入fSjbh"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="isvalid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isvalid">
              <a-input v-model="model.isvalid" placeholder="请输入isvalid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="status" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input v-model="model.status" placeholder="请输入status"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="submittime" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="submittime">
              <j-date placeholder="请选择submittime" v-model="model.submittime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉力" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zll">
              <a-input v-model="model.zll" placeholder="请输入张拉力"  ></a-input>
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
    name: 'WYanduSForm',
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
          add: "/ydcx/wYanduS/add",
          edit: "/ydcx/wYanduS/edit",
          queryById: "/ydcx/wYanduS/queryById"
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