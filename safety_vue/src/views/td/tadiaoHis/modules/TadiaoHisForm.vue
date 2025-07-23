<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devicesn">
              <a-input v-model="model.devicesn" placeholder="请输入设备编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回转" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="angle">
              <a-input v-model="model.angle" placeholder="请输入回转"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="吊绳倍率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fall">
              <a-input v-model="model.fall" placeholder="请输入吊绳倍率"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="吊钩高度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="height">
              <a-input v-model="model.height" placeholder="请输入吊钩高度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="吊重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="heavy">
              <a-input v-model="model.heavy" placeholder="请输入吊重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="力矩百分比" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="percent">
              <a-input v-model="model.percent" placeholder="请输入力矩百分比"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角角度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="obliqueangle">
              <a-input v-model="model.obliqueangle" placeholder="请输入倾角角度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角方向" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="obliquedirection">
              <a-input v-model="model.obliquedirection" placeholder="请输入倾角方向"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风速" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="windspeed">
              <a-input v-model="model.windspeed" placeholder="请输入风速"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="幅度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="radius">
              <a-input v-model="model.radius" placeholder="请输入幅度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devicetime">
              <j-date placeholder="请选择设备数据时间" v-model="model.devicetime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="司机身份证编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="driverid">
              <a-input v-model="model.driverid" placeholder="请输入司机身份证编号"  ></a-input>
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
    name: 'TadiaoHisForm',
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
          add: "/TadiaoHis/tadiaoHis/add",
          edit: "/TadiaoHis/tadiaoHis/edit",
          queryById: "/TadiaoHis/tadiaoHis/queryById"
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