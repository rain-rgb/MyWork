<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="节点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nodeid">
              <a-input-number v-model="model.nodeid" placeholder="请输入节点" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tem">
              <a-input v-model="model.tem" placeholder="请输入温度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="湿度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hum">
              <a-input v-model="model.hum" placeholder="请输入湿度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="记录时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recordtime">
              <j-date placeholder="请选择记录时间" v-model="model.recordtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="coordinatetype" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="coordinatetype">
              <a-input v-model="model.coordinatetype" placeholder="请输入coordinatetype"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lng">
              <a-input v-model="model.lng" placeholder="请输入经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lat">
              <a-input v-model="model.lat" placeholder="请输入纬度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="temh" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temh">
              <a-input v-model="model.temh" placeholder="请输入temh"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="teml" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="teml">
              <a-input v-model="model.teml" placeholder="请输入teml"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="humh" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="humh">
              <a-input v-model="model.humh" placeholder="请输入humh"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="huml" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="huml">
              <a-input v-model="model.huml" placeholder="请输入huml"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deviceno">
              <a-input v-model="model.deviceno" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="remar" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remar">
              <a-input v-model="model.remar" placeholder="请输入remar"  ></a-input>
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
    name: 'BhzSncTemForm',
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
          add: "/bhzsnctem/bhzSncTem/add",
          edit: "/bhzsnctem/bhzSncTem/edit",
          queryById: "/bhzsnctem/bhzSncTem/queryById"
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