<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devaddr">
              <a-input v-model="model.devaddr" placeholder="请输入设备地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="PM10(ug/m³)（最高200）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm10">
              <a-input v-model="model.pm10" placeholder="请输入PM10(ug/m³)（最高200）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="PM2.5(ug/m³)（最高500）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm25">
              <a-input v-model="model.pm25" placeholder="请输入PM2.5(ug/m³)（最高500）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="噪声(dB)（最高不高于100）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="noise">
              <a-input v-model="model.noise" placeholder="请输入噪声(dB)（最高不高于100）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度(℃)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temperature">
              <a-input v-model="model.temperature" placeholder="请输入温度(℃)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="湿度(%RH)（最高100）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="humidity">
              <a-input v-model="model.humidity" placeholder="请输入湿度(%RH)（最高100）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风力（最高18）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wind">
              <a-input v-model="model.wind" placeholder="请输入风力（最高18）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风速(m/s) （最高不大于100）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="windspeed">
              <a-input v-model="model.windspeed" placeholder="请输入风速(m/s) （最高不大于100）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风向  （风向度最高360）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="winddirection">
              <a-input v-model="model.winddirection" placeholder="请输入风向  （风向度最高360）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据保存的时间点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="timevalue">
              <j-date placeholder="请选择数据保存的时间点" v-model="model.timevalue"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否删除（0：未删除，1：已删除）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdel">
              <a-input-number v-model="model.isdel" placeholder="请输入是否删除（0：未删除，1：已删除）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否报警:0=报警,1=不报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isCall">
              <a-input-number v-model="model.isCall" placeholder="请输入是否报警:0=报警,1=不报警" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预警手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yjPhones">
              <a-input v-model="model.yjPhones" placeholder="请输入预警手机号码"  ></a-input>
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
    name: 'DevicehistoryYujingForm',
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
          add: "/devicehistoryyujing/devicehistoryYujing/add",
          edit: "/devicehistoryyujing/devicehistoryYujing/edit",
          queryById: "/devicehistoryyujing/devicehistoryYujing/queryById"
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